package com.yu.api.fallbacks;


import com.yu.api.client.MemberClient;
import com.yu.api.vo.MemberVO;
import com.yu.common.constant.HttpStatus;
import com.yu.common.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MemberFallbackFactory implements FallbackFactory<MemberClient> {


    @Override
    public MemberClient create(Throwable cause) {
        return new MemberClient() {
            @Override
            public AjaxResult<MemberVO> getMemberById(Integer id) {
                log.error("会员服务异常,根据会员ID查询会员等级失败", cause);
                return AjaxResult.error("会员服务暂时不可用,ID查询失败");
            }

            @Override
            public AjaxResult<List<MemberVO>> listByMember() {
                log.error("会员服务异常,查询所有会员等级失败", cause);
                return AjaxResult.error("会员服务暂时不可用,查询所有会员等级失败");

            }
        };
    }
}
