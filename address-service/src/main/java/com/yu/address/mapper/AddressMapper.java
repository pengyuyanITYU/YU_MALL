package com.yu.address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.address.domain.po.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
