package com.yu.api.client;

import com.yu.api.dto.DeductLocalMoneyDTO;
import com.yu.api.fallbacks.UserFallbackFactory;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="yu-mall-user-service",path="/users",fallbackFactory = UserFallbackFactory.class)
public interface UserClient {

    @ApiOperation("扣减用户余额")
    @PutMapping("/money/deduct")
    AjaxResult deductMoney(@RequestBody DeductLocalMoneyDTO deductLocalMoneyDTO);
}
