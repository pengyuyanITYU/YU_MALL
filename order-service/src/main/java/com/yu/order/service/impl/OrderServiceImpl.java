package com.yu.order.service.impl;

import cn.hutool.cache.impl.FIFOCache;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.api.client.AddressClient;
import com.yu.api.po.Address;
import com.yu.common.domain.AjaxResult;
import com.yu.common.utils.CollUtils;
import com.yu.common.utils.UserContext;
import com.yu.order.domain.dto.OrderFormDTO;
import com.yu.order.domain.dto.UpdateOrderStatusDTO;
import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.enums.PayType;
import com.yu.order.domain.po.Order;
import com.yu.order.domain.po.OrderDetail;
import com.yu.order.domain.vo.OrderDetailVO;
import com.yu.order.domain.vo.OrderVO;
import com.yu.order.mapper.OrderMapper;
import com.yu.order.service.IOrderDetailService;
import com.yu.order.service.IOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final AddressClient addressClient;

    @Lazy
    @Autowired
    private IOrderDetailService orderDetailService;

    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderVO> listById(Long id) {
        Long userId = UserContext.getUser();
        List<Order> list = lambdaQuery().eq(Order::getUserId, userId).list();
        List<OrderVO> orderVOList = list.stream().map(order -> {
            OrderVO orderVO = new OrderVO();
            AjaxResult<Address> address = addressClient.getAddressById(order.getAddressId());
            List<OrderDetailVO> orderDetailVOList = orderDetailService.getByOrderId(order.getId());
            orderVO.setDetails(orderDetailVOList);
            orderVO.setId(order.getId());
            orderVO.setTotalFee(order.getTotalFee());
            orderVO.setPaymentType(order.getPaymentType());
            orderVO.setStatus(order.getStatus());
            orderVO.setCreateTime(order.getCreateTime());
            orderVO.setPayTime(order.getPayTime());
            orderVO.setConsignTime(order.getConsignTime());
            orderVO.setEndTime(order.getEndTime());
            orderVO.setReceiverContact(address.getData().getContact());
            orderVO.setReceiverMobile(address.getData().getMobile());
            orderVO.setReceiverAddress(address.getData().getStreet());
            return orderVO;
        }).collect(Collectors.toList());

        log.info("查询用户{}的订单{}", userId, orderVOList);
        return orderVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrder(OrderFormDTO orderFormDTO) {
        Long userId = UserContext.getUser();
        if(userId == null){
            throw new RuntimeException("用户未登录,请检查登录信息");
        }
        AjaxResult<Address> addressData = addressClient.getAddressById(orderFormDTO.getAddressId());
        Address address = addressData.getData();
        if(address == null){
            throw new RuntimeException("收货地址不存在");
        }
        String detailReceiverAddress = address.getProvince() + address.getCity() + address.getTown() + address.getStreet();
        Order order = new Order();
        order.setConsignTime(LocalDateTime.now().plusDays(1))
             .setUserId(userId)
              .setPaymentType(orderFormDTO.getPaymentType())
             .setCloseTime(LocalDateTime.now().plusMinutes(30L))
             .setStatus(OrderStatus.UNPAID.getValue())
             .setAddressId(orderFormDTO.getAddressId())
             .setReceiverContact(address.getContact())
             .setReceiverMobile(address.getMobile())
             .setReceiverAddress(detailReceiverAddress)
             .setTotalFee(orderFormDTO.getTotalFee());
        boolean save = save(order);
        if(!save){
            log.error("订单保存失败");
        }
        boolean result = orderDetailService.addOrderDetails(orderFormDTO.getDetails(), order.getId());
        String message = order.getId().toString();


        rabbitTemplate.convertAndSend("order-service.direct", "order-cancel",message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties()
                        .setDelay(60000);
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });


        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByOrderId(Long id) {
        removeById(id);
        orderDetailService.deleteByOrderId(id);
    }

    @Override
    public void deleteByOrderIds(List<Long> ids) {
        removeByIds(ids);
        orderDetailService.deleteByOrderIds(ids);
    }


    @Override
    public OrderVO getByOrderId(Long id) {
        Long userId = UserContext.getUser();
        Order order = lambdaQuery().eq(Order::getId, id).eq(Order::getUserId, userId).one();

        AjaxResult<Address> address = addressClient.getAddressById(order.getAddressId());
        List<OrderDetailVO> orderDetailVOList = orderDetailService.getByOrderId(order.getId());
        OrderVO orderVO = new OrderVO();
        orderVO.setDetails(orderDetailVOList);
        orderVO.setTotalFee(order.getTotalFee());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setStatus(order.getStatus());
        orderVO.setPayTime(order.getPayTime());
        orderVO.setConsignTime(order.getConsignTime());
        orderVO.setEndTime(order.getEndTime());
        orderVO.setCreateTime(order.getCreateTime());
        orderVO.setCloseTime(order.getCloseTime());
        orderVO.setId(order.getId());
        orderVO.setReceiverContact(address.getData().getContact());
        orderVO.setReceiverMobile(address.getData().getMobile());
        orderVO.setReceiverAddress(address.getData().getStreet());
        log.info("查询用户{}的订单{}", userId, orderVO);
        return orderVO;
    }

    @Override
    public boolean cancelOrder(Long id) {
        boolean update = lambdaUpdate().eq(Order::getId, id).set(Order::getStatus, 5).set(Order::getCloseTime, LocalDateTime.now()).set(Order::getUpdateTime, LocalDateTime.now()).update();
        return update;

    }

    @Override
    public void batchConsignOrders() {

        boolean update = lambdaUpdate()
                .eq(Order::getStatus, OrderStatus.PAID)
                .le(Order::getCreateTime, LocalDateTime.now())
                .set(Order::getStatus, OrderStatus.SHIPPED)
                .set(Order::getConsignTime, LocalDateTime.now())
                .update();
        if (!update){
            log.info("订单暂不满足发货条件（可能状态不是PAID或支付未满24小时）");
        }
    }

    @Override
    public boolean confirmOrder(Long id) {
        boolean update = lambdaUpdate().eq(Order::getId, id)
                .set(Order::getStatus, 4)
                .set(Order::getUpdateTime, LocalDateTime.now())
                .set(Order::getEndTime, LocalDateTime.now())
                .set(Order::getCloseTime, LocalDateTime.now())
                .update();
        return update;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) {
        OrderStatus status = updateOrderStatusDTO.getStatus();
        Long id = updateOrderStatusDTO.getId();
        PayType paymentType = updateOrderStatusDTO.getPaymentType();
        if(id == null){
            log.error("订单id不能为空");
            throw new RuntimeException("订单id不能为空");
        }
        if(updateOrderStatusDTO.getPaymentType() == null){
            log.error("订单支付方式不能为空");
            throw new RuntimeException("订单支付方式不能为空");
        }
        if(status == null){
            log.error("订单状态不能为空");
            throw new RuntimeException("订单状态不能为空");
        }
        boolean update = false;
        if(status == OrderStatus.PAID){
            update = lambdaUpdate().eq(Order::getId, id)
                    .set(Order::getStatus, status)
                    .set(Order::getPayTime, LocalDateTime.now())
                    .set(Order::getPaymentType,paymentType)
                    .set(Order::getUpdateTime, LocalDateTime.now())
                    .update();
        }
        return update;
    }

    @Override
    public void updateAllPayOrderStatus() {
        Long userId = UserContext.getUser();
        if(userId == null){
            log.error("用户未登录,请检查登录信息");
            throw new RuntimeException("用户未登录,请检查登录信息");
        }
        boolean update = lambdaUpdate()
                .eq(Order::getStatus, OrderStatus.PAID)
                .set(Order::getStatus, OrderStatus.SUCCESS)
                .set(Order::getUpdateTime, LocalDateTime.now())
                .set(Order::getConsignTime, LocalDateTime.now())
                .update();
        if(!update){
            log.error("更新订单状态失败");
            throw new RuntimeException("更新订单状态失败");
        }

    }
}
