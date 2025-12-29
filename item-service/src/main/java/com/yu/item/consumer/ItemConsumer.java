package com.yu.item.consumer;

import com.yu.item.domain.dto.OrderDetailDTO;
import com.yu.item.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemConsumer {

    private final IItemService itemService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item-service-sold.queue", durable = "true"), exchange = @Exchange(value = "order-item-service.direct", durable = "true"), key = "item-sold"))
    public void updateStockAndSold(List<OrderDetailDTO> orderDetailDTOList) {
        log.info("开始处理商品添加消息{}", orderDetailDTOList);
        itemService.updateStockAndSold(orderDetailDTOList);
    }


}
