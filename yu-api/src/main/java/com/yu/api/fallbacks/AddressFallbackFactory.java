package com.yu.api.fallbacks;

import com.yu.api.client.AddressClient;
import com.yu.api.po.Address;
import com.yu.common.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddressFallbackFactory implements FallbackFactory<AddressClient> {


    @Override
    public AddressClient create(Throwable cause) {
        return new AddressClient() {
            @Override
            public AjaxResult<Address> getAddressById(Long id) {
                log.error("地址服务异常,根据ID查询地址失败", cause);
                return AjaxResult.error("地址服务暂时不可用,根据地址ID查询地址失败");
            }
        };
    }
}
