package com.yu.api.fallbacks;


import com.yu.api.client.UserClient;
import com.yu.api.dto.DeductLocalMoneyDTO;
import com.yu.common.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public AjaxResult deductMoney(DeductLocalMoneyDTO deductLocalMoneyDTO) {
                log.error("用户服务,扣减用户余额失败", cause);
                return AjaxResult.error("用户服务,扣减用户余额失败");
            }
        };
    }
}
