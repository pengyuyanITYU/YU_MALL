package com.yu.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.item.domain.po.Item;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {
}
