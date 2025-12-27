package com.yu.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.order.domain.po.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
