package com.yu.order.controller;

import com.yu.common.domain.AjaxResult;
import com.yu.order.domain.dto.CommentDTO;
import com.yu.order.domain.dto.OrderFormDTO;
import com.yu.order.domain.dto.UpdateOrderStatusDTO;
import com.yu.order.domain.enums.OrderStatus;
import com.yu.order.domain.enums.PayType;
import com.yu.order.domain.po.Order;
import com.yu.order.domain.po.OrderDetail;
import com.yu.order.domain.vo.OrderDetailVO;
import com.yu.order.domain.vo.OrderVO;
import com.yu.order.service.IOrderDetailService;
import com.yu.order.service.IOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "订单服务")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;

    private final IOrderDetailService orderDetailService;

    @ApiOperation("查询用户订单列表")
    @GetMapping("/{id}/list")
    public AjaxResult<List<OrderVO>> listById(@PathVariable Long id) {
        log.info("开始进行列表查询");
        List<OrderVO> orders = orderService.listById(id);
        return AjaxResult.success(orders);
    }

    @ApiOperation("查询用户订单详情")
    @GetMapping("/{id}")
    @ApiImplicitParam(name = "id", value = "订单id")
    public AjaxResult<OrderVO> getByOrderId(@PathVariable Long id) {
        log.info("开始进行订单详情查询");
        OrderVO orderVO = orderService.getByOrderId(id);
        return AjaxResult.success(orderVO);
    }

    @ApiOperation("查询订单(服务内部调用)")
    @GetMapping("/getById/{id}")
    public AjaxResult<Order> getById(@PathVariable Long id) {
        log.info("开始进行订单查询");
        Order order = orderService.getById(id);
        return AjaxResult.success(order);
    }

    @ApiOperation("删除订单")
    @DeleteMapping("/{id}")
    public AjaxResult deleteById(@PathVariable Long id) {
        log.info("开始进行订单删除");
        orderService.deleteByOrderId(id);
        return AjaxResult.success();
    }

    @ApiOperation("批量删除订单")
    @ApiImplicitParam(name = "ids", value = "订单条目id集合")
    @DeleteMapping
    public AjaxResult deleteByIds(@RequestParam("ids") List<Long> ids){
        orderService.deleteByOrderIds(ids);
        return AjaxResult.success();
    }

    @ApiOperation("添加订单")
    @PostMapping
    public AjaxResult addOrder(@RequestBody @Valid OrderFormDTO  orderFormDTO){
        log.info("开始添加订单{}",orderFormDTO);
        return AjaxResult.toAjax(orderService.addOrder(orderFormDTO));
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public AjaxResult cancelOrder(@PathVariable Long id){
        log.info("开始取消订单{}",id);
        return AjaxResult.toAjax(orderService.cancelOrder(id));
    }

    @ApiOperation("确认收货")
    @PutMapping("/confirm/{id}")
    public AjaxResult confirmOrder(@PathVariable Long id){
        log.info("开始确认收货{}",id);
        return AjaxResult.toAjax(orderService.confirmOrder(id));
    }

    @ApiOperation("更改订单状态")
    @PutMapping("/updateStatus/")
    public AjaxResult updateOrderStatus(@RequestBody UpdateOrderStatusDTO updateOrderStatusDTO){
        log.info("开始更改订单状态{}",updateOrderStatusDTO);
        return AjaxResult.toAjax(orderService.updateOrderStatus(updateOrderStatusDTO));
    }

    @ApiOperation("更改订单为已评价")
    @PutMapping("/commented")
    public AjaxResult<Void> updateOrderCommented(@RequestBody CommentDTO commentDTO){
        log.info("更改订单评价状态为已评价");
        orderDetailService.updateOrderCommented(commentDTO);
        return AjaxResult.success();
    }


}
