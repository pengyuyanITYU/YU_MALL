package com.yu.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.order.domain.dto.CommentDTO;
import com.yu.order.domain.dto.OrderFormDTO;
import com.yu.order.domain.dto.UpdateOrderStatusDTO;
import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.po.Order;
import com.yu.order.domain.vo.OrderVO;


import java.util.List;

public interface IOrderService extends IService<Order> {

    List<OrderVO> listById(Long id);

    boolean addOrder(OrderFormDTO orderFormDTO);

    void deleteByOrderId(Long id);

    void deleteByOrderIds(List<Long> ids);

    OrderVO getByOrderId(Long id);

    void batchConsignOrders();

    boolean cancelOrder(Long id);

    boolean confirmOrder(Long id);

    boolean updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO);

    void updateAllPayOrderStatus();


}
