package com.yu.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.order.domain.dto.CommentDTO;
import com.yu.order.domain.dto.OrderDetailDTO;
import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.po.OrderDetail;
import com.yu.order.domain.vo.OrderDetailVO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public interface IOrderDetailService extends IService<OrderDetail> {


    List<OrderDetailVO> getByOrderId(Long id);

    boolean addOrderDetails( List<OrderDetailDTO> orderDetailDTOList, Long orderId);

    void deleteByOrderId(Long id);

    void deleteByOrderIds(List<Long> ids);

    void updateOrderCommented(CommentDTO commentDTO);
}
