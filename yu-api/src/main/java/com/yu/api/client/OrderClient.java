package com.yu.api.client;

import com.yu.api.dto.CommentDTO;
import com.yu.api.dto.UpdateOrderStatusDTO;
import com.yu.api.enums.OrderStatus;
import com.yu.api.fallbacks.OrderFallbackFactory;
import com.yu.api.po.Order;
import com.yu.common.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="yu-mall-order-service",path="/orders",fallbackFactory = OrderFallbackFactory.class)
public interface OrderClient {

    @PutMapping("/updateStatus/{id}")
    AjaxResult<Void> updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO);

    @PutMapping("/commented")
    AjaxResult<Void> updateOrderCommented(@RequestBody CommentDTO commentDTO);

    @GetMapping("/getById/{id}")
    AjaxResult<Order> getById(@PathVariable Long id);
}
