package com.yu.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.api.client.OrderClient;
import com.yu.api.client.UserClient;
import com.yu.api.enums.OrderStatus;
import com.yu.common.utils.UserContext;
import com.yu.pay.domain.dto.PayOrderFormDTO;
import com.yu.pay.domain.po.PayOrder;
import com.yu.pay.enums.PayStatus;
import com.yu.pay.mapper.PayOrderMapper;
import com.yu.pay.service.IPayOrderService;
import com.yu.pay.utils.OrderIdGenerator;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    private final OrderClient orderClient;

    private final UserClient userClient;

    private final RabbitTemplate rabbitTemplate;

    @Override
    @GlobalTransactional(name = "yu-mall-pay-service", rollbackFor = Exception.class)
    public void payOrder(PayOrderFormDTO payOrderFormDTO) {
        Long userId  = UserContext.getUser();
        if(userId == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录,请检查登录状态");
        }
        PayOrder payOrderByOrderId = getPayOrderByOrderId(payOrderFormDTO.getBizOrderNo());
        boolean save;
        PayOrder payOrder = new PayOrder();
        if(payOrderByOrderId == null){
            String pay_order_no = OrderIdGenerator.generatePayOrderNo();
            payOrder.setPayOrderNo(Long.valueOf(pay_order_no))
                    .setPaySuccessTime(LocalDateTime.now())
                    .setBizOrderNo(payOrderFormDTO.getBizOrderNo())
                    .setBizUserId(userId)
                    .setAmount(payOrderFormDTO.getAmount())
                    .setStatus(PayStatus.TRADE_SUCCESS.getValue())
                    .setPayType(payOrderFormDTO.getPaymentType())
                    .setPayOverTime(LocalDateTime.now().plusMinutes(30))
                    .setPaySuccessTime(LocalDateTime.now());
            save = save(payOrder);
            if (!save){
                log.error("保存支付订单失败");
                throw new RuntimeException("保存支付订单失败");
            }
        }
        try{
            rabbitTemplate.convertAndSend("pay-service.topic", "pay-order-balance", payOrderFormDTO);
        }catch (Exception e){
            log.error("支付失败:{}",e.getMessage());
            throw new RuntimeException("支付失败");
        }

    }



    @Override
    public PayOrder getPayOrderByOrderId(Long orderBizId) {
        if(orderBizId == null){
            log.error("订单不存在");
            throw new RuntimeException("订单不存在");
        }
        PayOrder one = lambdaQuery().eq(PayOrder::getBizOrderNo, orderBizId).one();
        return one;
    }
}
