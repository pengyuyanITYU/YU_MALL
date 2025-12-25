package com.yu.api.config;

import com.yu.api.fallbacks.ItemFallbackFactory;
import com.yu.api.fallbacks.MemberFallbackFactory;
import com.yu.api.fallbacks.OrderFallbackFactory;
import com.yu.api.fallbacks.UserFallbackFactory;
import com.yu.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
public class DefaultFeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 获取登录用户
                Long userId = UserContext.getUser();
                if(userId == null) {
                    // 如果为空则直接跳过
                    return;
                }
                // 如果不为空则放入请求头中，传递给下游微服务
                template.header("user-info", userId.toString());
            }
        };
    }

    @Bean
    public MemberFallbackFactory memberFallbackFactory(){
        return new MemberFallbackFactory();
    }

    @Bean
    public UserFallbackFactory userFallbackFactory(){
        return new UserFallbackFactory();
    }

    @Bean
    public OrderFallbackFactory orderFallbackFactory(){
        return new OrderFallbackFactory();
    }

    @Bean
    public ItemFallbackFactory itemFallbackFactory(){
        return new ItemFallbackFactory();
    }
}
