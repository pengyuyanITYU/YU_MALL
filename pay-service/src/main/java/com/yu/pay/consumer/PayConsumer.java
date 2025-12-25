package com.yu.pay.consumer;

import com.yu.api.client.OrderClient;
import com.yu.api.client.UserClient;
import com.yu.api.dto.DeductLocalMoneyDTO;
import com.yu.api.dto.UpdateOrderStatusDTO;
import com.yu.api.enums.OrderStatus;
import com.yu.api.enums.PayType;
import com.yu.api.po.Order;
import com.yu.common.domain.AjaxResult;
import com.yu.pay.domain.dto.PayOrderFormDTO;
import com.yu.pay.domain.po.PayOrder;
import com.yu.pay.enums.PayStatus;
import com.yu.pay.service.IPayOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayConsumer {

    private final UserClient userClient;

    private final OrderClient orderClient;

    private final IPayOrderService payOrderService;

    private final RabbitTemplate rabbitTemplate;


    @RabbitListener(bindings=@QueueBinding(value=@Queue(value = "pay-service.queue",durable = "true"),exchange = @Exchange(value = "pay-service.topic",durable = "true",type = "topic"),key = "pay-order-balance"))
    @GlobalTransactional(rollbackFor = Exception.class)
    public void payOrder(PayOrderFormDTO payOrderFormDTO, Message  message){
        log.info("接收到消息:{}",payOrderFormDTO);
        try{
            // 【第一重校验：快速失败】在事务外部查询，过滤绝大多数重复请求，减轻数据库事务压力
            AjaxResult<Order> byId = orderClient.getById(payOrderFormDTO.getBizOrderNo());
            Order data = byId.getData();
            if(data == null || !byId.isSuccess() ){
                log.warn("提前校验拦截：订单 {}的状态为{}, 跳过处理", payOrderFormDTO.getBizOrderNo(), OrderStatus.of(data.getStatus()) == null ? "未知" : OrderStatus.of(data.getStatus()));
                return;
            }

            PayOrder payOrderByOrderId = payOrderService.getPayOrderByOrderId(payOrderFormDTO.getBizOrderNo());
            if(payOrderByOrderId != null && OrderStatus.UNPAID != OrderStatus.of(payOrderByOrderId.getStatus()) && OrderStatus.of(data.getStatus()) != OrderStatus.UNPAID){
                log.warn("提前校验拦截：订单 {} 已处理, 跳过处理", payOrderFormDTO.getBizOrderNo());
                return;
            }
            DeductLocalMoneyDTO deductLocalMoneyDTO = DeductLocalMoneyDTO.builder()
                    .orderBizId(payOrderFormDTO.getBizOrderNo())
                    .payPassword(payOrderFormDTO.getPayPassword())
                    .amount(payOrderFormDTO.getAmount())
                    .userId(data.getUserId())
                    .build();
            userClient.deductMoney(deductLocalMoneyDTO);
            UpdateOrderStatusDTO  updateOrderStatusDTO = new UpdateOrderStatusDTO();
            PayType payType = PayType.of(payOrderFormDTO.getPaymentType());
            if(payType == null){
                log.error("支付类型不能为null错误");
                throw new RuntimeException("支付类型不能为null错误");
            }
            updateOrderStatusDTO.setId(payOrderFormDTO.getBizOrderNo())
                    .setStatus(OrderStatus.PAID)
                    .setPaymentType(payType);
            orderClient.updateOrderStatus(updateOrderStatusDTO);
            log.info("支付成功");
        }catch (Exception e){
            log.error("支付失败:{}",e.getMessage());
            throw new RuntimeException("支付失败");
        }


    }
}
