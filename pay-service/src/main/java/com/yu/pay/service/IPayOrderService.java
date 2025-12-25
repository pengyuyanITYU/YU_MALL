package com.yu.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.pay.domain.dto.PayApplyDTO;
import com.yu.pay.domain.dto.PayOrderFormDTO;
import com.yu.pay.domain.po.PayOrder;


public interface IPayOrderService extends IService<PayOrder> {


    void payOrder(PayOrderFormDTO payOrderFormDTO);

    PayOrder getPayOrderByOrderId(Long orderBizId);

}
