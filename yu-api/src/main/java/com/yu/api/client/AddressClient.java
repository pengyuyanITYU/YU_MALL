package com.yu.api.client;

import com.yu.api.fallbacks.AddressFallbackFactory;
import com.yu.api.po.Address;
import com.yu.common.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="yu-mall-address-service",path="/addresses",fallbackFactory = AddressFallbackFactory.class)
public interface AddressClient {

    @GetMapping("/{id}")
    AjaxResult<Address> getAddressById(@PathVariable("id") Long id);
}
