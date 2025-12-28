package com.yu.order.task;


import com.yu.order.service.IOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Deprecated
public class OrderConfirmTimeTask {

    private final IOrderService orderService;

//    @ApiOperation("收获")
//    @Scheduled(cron = "0 * * * * ?")
//    @SchedulerLock(name = "orderConsignTimeTask", lockAtMostFor = "1m", lockAtLeastFor = "30s")
//    public void orderConsignTimeTask() throws ClassNotFoundException, NoSuchMethodException {
//
//        log.info("开始执行定时任务:按时执行更新订单为收货操作");
//        try{
//            orderService.batchConsignOrders();
//        }catch (Exception e){
//            log.error("执行定时任务:按时执行更新订单为发货操作异常",e);
//        }
//    }
}
