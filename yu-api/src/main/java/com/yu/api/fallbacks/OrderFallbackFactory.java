package com.yu.api.fallbacks;

import com.yu.api.client.OrderClient;
import com.yu.api.dto.CommentDTO;
import com.yu.api.dto.UpdateOrderStatusDTO;
import com.yu.api.enums.OrderStatus;
import com.yu.api.po.Order;
import com.yu.common.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderFallbackFactory implements FallbackFactory<OrderClient> {


    @Override
    public OrderClient create(Throwable cause) {
        return new OrderClient() {
            @Override
            public AjaxResult updateOrderStatus(UpdateOrderStatusDTO updateOrderStatusDTO) {
                log.error("订单服务,更改订单状态失败", cause);
                return AjaxResult.error("订单服务暂时不可用,更改订单状态失败");
            }

            @Override
            public AjaxResult<Void> updateOrderCommented(CommentDTO commentDTO) {
                log.error("订单服务,更改订单为已评价失败", cause);
                return AjaxResult.error("订单服务暂时不可用,更改订单为已评价失败");
            }

            @Override
            public AjaxResult<Order> getById(Long id) {
                log.error("订单服务,查询{}订单失败",id, cause);
                return AjaxResult.error("订单服务暂时不可用,查询订单失败");
            }
        };
    }
}
