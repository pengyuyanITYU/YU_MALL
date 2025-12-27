package com.yu.address.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.address.domain.dto.AddressDTO;
import com.yu.address.domain.po.Address;
import com.yu.address.mapper.AddressMapper;
import com.yu.address.service.IAddressService;
import com.yu.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    public void updateByAddressId(Address  address) {
        log.info("开始修改地址");
        if(address.getId() == null){
            throw new RuntimeException("id不能为空");
        }
        updateById(address);
    }

    @Override
    public List<Address> listAddress() {
        Long userId = UserContext.getUser();
        if(userId == null){
            throw new RuntimeException("未获取到用户信息，请检查登录状态");
        }
        List<Address> list = lambdaQuery().eq(Address::getUserId, userId).list();
        log.info("{}",list);
       return list();
    }


    @Override
    public void addAddress(AddressDTO addressDTO) {
        Long userId = UserContext.getUser();
        if(userId == null){
            throw new RuntimeException("未获取到用户信息，请检查登录状态");
        }
        Address address = new Address()
                .setUserId(userId)
                .setProvince(addressDTO.getProvince())
                .setCity(addressDTO.getCity())
                .setTown(addressDTO.getTown())
                .setMobile(addressDTO.getMobile())
                .setStreet(addressDTO.getStreet())
                .setContact(addressDTO.getContact());
        save(address);
    }
}
