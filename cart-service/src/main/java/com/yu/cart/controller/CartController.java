package com.yu.cart.controller;

import com.yu.cart.domain.dto.CartFormDTO;
import com.yu.cart.domain.po.Cart;
import com.yu.cart.domain.vo.CartVO;
import com.yu.cart.service.ICartService;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final ICartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping
    public AjaxResult addItemCart(@Valid @RequestBody CartFormDTO cartFormDTO){
        log.info("添加{}商品到购物车", cartFormDTO);
        cartService.addItemCart(cartFormDTO);
        return AjaxResult.success();
    }

    @ApiOperation("更新购物车数据")
    @PutMapping
    public AjaxResult updateCart(@RequestBody Cart cart){
        cartService.updateById(cart);
        return AjaxResult.success();
    }

    @ApiOperation("删除购物车中商品")
    @DeleteMapping("{id}")
    public AjaxResult deleteCartItem(@Param ("购物车条目id")@PathVariable("id") Long id){
        return AjaxResult.toAjax(cartService.removeById(id));
    }

    @ApiOperation("查询购物车列表")
    @GetMapping
    public AjaxResult queryMyCarts(@RequestHeader(name = "user-info",required = false)String userInfo){
        log.info("查询用户{}的购物车列表", userInfo);
        return AjaxResult.success(cartService.queryMyCarts());
    }


    @ApiOperation("批量删除购物车中商品")
    @ApiImplicitParam(name = "ids", value = "购物车条目id集合")
    @DeleteMapping
    public AjaxResult deleteCartItemByIds(@RequestParam("ids") List<Long> ids){
      return AjaxResult.toAjax(cartService.removeByIds(ids));
    }
}
