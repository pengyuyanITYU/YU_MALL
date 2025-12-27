package com.yu.cart.service;

import com.yu.cart.domain.dto.CartFormDTO;
import com.yu.cart.domain.po.Cart;
import com.yu.cart.domain.vo.CartVO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Collection;
import java.util.List;

public interface ICartService extends IService<Cart> {

    void addItemCart(CartFormDTO cartFormDTO);

    List<CartVO> queryMyCarts();

}
