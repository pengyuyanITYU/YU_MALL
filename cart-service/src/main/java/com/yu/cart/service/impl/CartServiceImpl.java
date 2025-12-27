package com.yu.cart.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.api.client.ItemClient;
import com.yu.api.vo.ItemDetailVO;
import com.yu.cart.domain.dto.CartFormDTO;
import com.yu.cart.domain.po.Cart;
import com.yu.cart.domain.vo.CartVO;
import com.yu.cart.mapper.CartMapper;
import com.yu.cart.service.ICartService;
import com.yu.common.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.tools.rmi.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Api(tags = "购物车相关接口")
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {



    private final ItemClient itemClient;

    @Override
    @ApiOperation("添加商品到购物车")
    public void addItemCart(CartFormDTO cartFormDTO) {
        // 1.获取登录用户
        Long userId = UserContext.getUser();

        if (userId == null) {
            throw new RuntimeException("未获取到用户信息，请检查登录状态");
        }

        ItemDetailVO item = itemClient.getItemById(cartFormDTO.getItemId()).getData();
        if (item == null){
            throw new RuntimeException("商品不存在");
        }
        Cart c = lambdaQuery().eq(Cart::getUserId, userId).eq(Cart::getItemId, cartFormDTO.getItemId()).one();
        if(c != null){
            c.setNum(c.getNum() + cartFormDTO.getNum());
            updateById(c);
            return;
        }
        // 确保cartFormDTO包含name字段
        if (cartFormDTO.getName() == null) {
            cartFormDTO.setName(item.getName());
        }
        Cart cart = new Cart();
        if(userId != null){
            cart = new Cart()
                    .setItemId(cartFormDTO.getItemId())
                    .setNum(cartFormDTO.getNum())
                    .setImage(cartFormDTO.getImage())
                    .setSpec(cartFormDTO.getSpec())
                    .setPrice(cartFormDTO.getPrice())
                    .setName(cartFormDTO.getName())
                    .setUserId(userId);
        }
        System.out.println("准备保存购物车数据: " + cart);
        save(cart);
    }

    @Override
    public List<CartVO> queryMyCarts() {
        Long userId = UserContext.getUser();
        List<Cart> list = lambdaQuery().eq(Cart::getUserId, userId).list();
        List<CartVO> carts = list.stream().map(cart -> {
            CartVO cartVO = new CartVO();
            cartVO.setId(cart.getId());
            cartVO.setItemId(cart.getItemId());
            cartVO.setNum(cart.getNum());
            cartVO.setName(cart.getName());
            cartVO.setSpec(cart.getSpec());
            cartVO.setPrice(cart.getPrice());
            cartVO.setImage(cart.getImage());
            return cartVO;
        }).collect(Collectors.toList());
        return carts;
    }
}
