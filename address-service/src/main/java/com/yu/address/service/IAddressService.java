package com.yu.address.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.address.domain.dto.AddressDTO;
import com.yu.address.domain.po.Address;

import java.util.List;

public interface IAddressService extends IService<Address> {
    void updateByAddressId(Address address);

    List<Address> listAddress();

    void addAddress(AddressDTO addressDTO);
}
