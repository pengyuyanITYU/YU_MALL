package com.yu.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.pay.domain.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder> {

}
