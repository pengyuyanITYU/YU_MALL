package com.yu.pay.controller;

import com.yu.common.domain.AjaxResult;
import com.yu.pay.domain.dto.PayOrderFormDTO;
import com.yu.pay.domain.po.Order;
import com.yu.pay.domain.po.PayOrder;
import com.yu.pay.service.IPayOrderService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Api(tags = "支付相关接口")
@RestController
@RequestMapping("pay-orders")
@RequiredArgsConstructor
@Slf4j
public class PayController {

    private final IPayOrderService payOrderService;

    @ApiOperation("支付订单")
    @PutMapping
    public AjaxResult payOrder(@Valid @RequestBody PayOrderFormDTO payOrderForm) {
        log.info("开始支付订单{}", payOrderForm);
        payOrderService.payOrder(payOrderForm);
        return AjaxResult.success();
    }

    @ApiOperation("根据订单ID查询支付流水")
    @GetMapping("/{orderBizId}")
    public AjaxResult<PayOrder> getPayOrderByOrderId(@PathVariable Long orderBizId){
        log.info("查询支付订单{}", orderBizId);
        return AjaxResult.success(payOrderService.getPayOrderByOrderId(orderBizId));
    }
}
