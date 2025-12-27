package com.yu.api.client;


import com.yu.api.fallbacks.MemberFallbackFactory;
import com.yu.api.vo.MemberVO;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="yu-mall-member-service",path="/members",fallbackFactory = MemberFallbackFactory.class)
public interface MemberClient {

    @GetMapping("/{id}")
    AjaxResult<MemberVO> getMemberById(@PathVariable Integer id);

    @ApiOperation("查询所有会员")
    @GetMapping
    AjaxResult<List<MemberVO>> listByMember();
}
