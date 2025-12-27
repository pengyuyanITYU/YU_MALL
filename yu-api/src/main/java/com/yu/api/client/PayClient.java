package com.yu.api.client;


import com.yu.api.dto.PayOrderDTO;
import com.yu.api.po.PayOrder;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "yu-mall-pay-service", path = "pay-orders")
public interface PayClient {

    @ApiOperation("根据订单ID查询支付流水")
    @GetMapping("/{orderBizId}")
    AjaxResult<PayOrder> getPayOrderByOrderId(@PathVariable Long orderBizId);
}
