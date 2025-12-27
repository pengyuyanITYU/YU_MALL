package com.yu.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.api.client.ItemClient;
import com.yu.api.client.UserClient;
import com.yu.api.vo.ItemDetailVO;
import com.yu.common.domain.AjaxResult;
import com.yu.common.utils.UserContext;
import com.yu.order.domain.dto.CommentDTO;
import com.yu.order.domain.dto.OrderDetailDTO;
import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.po.Order;
import com.yu.order.domain.po.OrderDetail;
import com.yu.order.domain.vo.OrderDetailVO;
import com.yu.order.mapper.OrderDetailMapper;
import com.yu.order.service.IOrderDetailService;
import com.yu.order.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    private final ItemClient itemClient;

    @Autowired
    @Lazy
    private  IOrderService orderService;

    @Override
    public List<OrderDetailVO> getByOrderId(Long id) {
        log.info("开始查询订单{}的详情", id);
        List<OrderDetail> list = lambdaQuery().eq(OrderDetail::getOrderId, id).list();
        List<OrderDetailVO> orderDetailVOList = list.stream().map(
                orderDetail -> {
                    OrderDetailVO orderDetailVO = new OrderDetailVO();
                    orderDetailVO.setId(orderDetail.getId());
                    orderDetailVO.setItemId(orderDetail.getItemId());
                    orderDetailVO.setNum(orderDetail.getNum());
                    orderDetailVO.setName(orderDetail.getName());
                    orderDetailVO.setSpec(orderDetail.getSpec());
                    orderDetailVO.setPrice(orderDetail.getPrice());
                    orderDetailVO.setImage(orderDetail.getImage());
                    return orderDetailVO;
                }
        ).collect(Collectors.toList());
        log.info("查询订单详情{}", orderDetailVOList);
        return orderDetailVOList;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addOrderDetails(@Valid @NotEmpty(message = "订单商品不能为空") List<OrderDetailDTO>  orderDetailDTOList, Long orderId) {
        log.info("开始添加订单{}的详情", orderDetailDTOList);
        List<OrderDetail> orderDetailList = orderDetailDTOList.stream().map(orderDetailDTO -> {
            AjaxResult<ItemDetailVO> itemData = itemClient.getItemById(orderDetailDTO.getItemId());
            if (!itemData.isSuccess()) {
                log.error("商品{}不存在:{}", orderDetailDTO.getItemId(), itemData.getCode());
                throw new RuntimeException("商品不存在");
            }
            ItemDetailVO item = itemData.getData();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId)
                    .setItemId(item.getId())
                    .setNum(orderDetailDTO.getNum())
                    .setName(item.getName())
                    .setSpec(orderDetailDTO.getSpecs())
                    .setImage(orderDetailDTO.getImage())
                    .setPrice(orderDetailDTO.getPrice());
            return orderDetail;
        }).collect(Collectors.toList());
        boolean result = saveBatch(orderDetailList);
        if(!result){
            log.error("添加订单{}的详情失败", orderDetailList);
            throw new RuntimeException("添加订单详情失败");
        }
        log.info("添加订单{}的详情成功", orderDetailList);
        return result;
    }

    @Override
    public void deleteByOrderId(Long id) {
        boolean remove = lambdaUpdate().eq(OrderDetail::getOrderId, id).remove();
        if(!remove){
            log.error("删除订单{}的详情失败", id);
            throw new RuntimeException("删除订单详情失败");
        }
    }

    @Override
    public void deleteByOrderIds(List<Long> ids) {
        boolean remove = lambdaUpdate().in(OrderDetail::getOrderId, ids).remove();
        if(!remove){
            log.error("批量删除订单{}的详情失败", ids);
            throw new RuntimeException("批量删除订单详情失败");
        }
    }

    @Override
    public void updateOrderCommented(CommentDTO commentDTO) {
        OrderDetail one = lambdaQuery().eq(commentDTO.getOrderId() != null, OrderDetail::getOrderId, commentDTO.getOrderId())
                .eq(commentDTO.getOrderDetailId() != null, OrderDetail::getId, commentDTO.getOrderDetailId())
                .eq(commentDTO.getItemId() != null, OrderDetail::getItemId, commentDTO.getItemId())
                .one();
        if (one == null){
            log.error("订单{}的详情{}不存在", commentDTO.getOrderId(), commentDTO.getOrderDetailId());
            throw new RuntimeException("订单详情不存在");
        }
        one.setCommented(true);
        boolean update = updateById(one);
        if (!update){
            log.error("更新订单{}的详情{}失败", commentDTO.getOrderId(), commentDTO.getOrderDetailId());
            throw new RuntimeException("更新订单详情失败");
        }
        boolean updateOrder = orderService.lambdaUpdate().eq(Order::getId, commentDTO.getOrderId())
                .set(Order::getStatus, OrderStatus.EVALUATED)
                .set(Order::getCommentTime, LocalDateTime.now())
                .set(Order::getEndTime, LocalDateTime.now())
                .update();
        if (!updateOrder){
            log.error("更新订单{}失败", commentDTO.getOrderId());
            throw new RuntimeException("更新订单失败");
        }
    }
}
