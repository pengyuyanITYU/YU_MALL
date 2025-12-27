package com.yu.member.controller;

import java.util.List;
import com.yu.common.domain.AjaxResult;
import com.yu.member.domain.vo.MemberVO;
import com.yu.member.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
@Api("会员模块")
public class MemberController {

    private final IMemberService memberService;

    @ApiOperation("根据会员ID查询")
    @GetMapping("/{id}")
    public AjaxResult<MemberVO> getMemberById(@PathVariable Integer id) {
        log.info("开始查询会员id{}",id);
        return AjaxResult.success(memberService.getMemberById(id));
    }

    @ApiOperation("查询所有会员")
    @GetMapping
    public AjaxResult<List<MemberVO>> listByMember() {
        return AjaxResult.success(memberService.listByMember());
    }
}
