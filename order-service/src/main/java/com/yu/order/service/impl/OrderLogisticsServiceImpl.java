package com.yu.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.order.domain.po.OrderLogistics;
import com.yu.order.mapper.OrderLogisticsMapper;
import com.yu.order.service.IOrderLogistics;
import org.springframework.stereotype.Service;


@Service
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements IOrderLogistics {
}
