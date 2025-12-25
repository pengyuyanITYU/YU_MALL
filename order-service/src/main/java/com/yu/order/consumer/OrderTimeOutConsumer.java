package com.yu.order.consumer;


import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.po.Order;
import com.yu.order.domain.vo.OrderVO;
import com.yu.order.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderTimeOutConsumer {

    private final IOrderService orderService;

    @RabbitListener(bindings=@QueueBinding(value=@Queue(value="order-cancel.queue",durable = "true"),exchange=@Exchange(value="order-service.direct",delayed = "true"),key="order-cancel"))
    public void timeOut(String msg){
        log.info("订单超时,订单ID:{}", msg);
        Order order = new Order();
        order.setId(Long.parseLong(msg));
        Order byOrderId = orderService.getById(order);
        if (byOrderId == null){
            log.error("订单不存在");
            return;
        }
        if(byOrderId.getStatus() != 1){
            log.info("订单已支付");
            return;
        }
        orderService.cancelOrder(byOrderId.getId());
    }
}
