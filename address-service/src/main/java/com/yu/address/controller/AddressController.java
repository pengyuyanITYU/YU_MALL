package com.yu.address.controller;

import com.yu.address.domain.dto.AddressDTO;
import com.yu.address.domain.po.Address;
import com.yu.address.service.IAddressService;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api("地址模块")
@RestController
@RequestMapping("/addresses")
@Slf4j
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @GetMapping
    @ApiOperation("查询地址列表")
    public AjaxResult<List<Address>> list(){
        log.info("开始查询地址列表");
        return AjaxResult.success(addressService.listAddress());
    }

    @PutMapping
    @ApiOperation("修改地址")
    public AjaxResult update(@RequestBody Address address){
        log.info("开始修改地址");
        addressService.updateByAddressId(address);
        return AjaxResult.success();
    }

    @PostMapping
    @ApiOperation("添加地址")
    public AjaxResult addAddress(@RequestBody AddressDTO addressDTO){
        log.info("开始添加地址{}", addressDTO);
        addressService.addAddress(addressDTO);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除地址")
    public AjaxResult deleteAddressById(@PathVariable Long id){
        log.info("开始删除地址");
        addressService.removeById(id);
        return AjaxResult.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("查询地址")
    public AjaxResult<Address> getAddressById(@PathVariable Long id){
        log.info("开始查询地址id{}",id);
        return AjaxResult.success(addressService.getById(id));
    }


}
