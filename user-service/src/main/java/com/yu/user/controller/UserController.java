package com.yu.user.controller;


import com.yu.common.domain.AjaxResult;
import com.yu.common.utils.BeanUtils;
import com.yu.common.utils.UserContext;
import com.yu.user.domain.dto.*;
import com.yu.user.domain.po.User;
import com.yu.user.domain.vo.UserLoginVO;
import com.yu.user.domain.vo.UserRegisterVO;
import com.yu.user.domain.vo.UserVO;
import com.yu.user.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@Api("用户模块")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public AjaxResult<UserLoginVO> login(@RequestBody @Valid LoginFormDTO loginFormDTO){
        log.info("用户登录：{}", loginFormDTO);
        return AjaxResult.success(userService.login(loginFormDTO));
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public AjaxResult<UserRegisterVO> register(@RequestBody @Valid RegisterFormDTO registerFormDTO){
        log.info("用户注册：{}", registerFormDTO);
        return AjaxResult.success(userService.register(registerFormDTO));
    }

    @ApiOperation("更改用户信息")
    @PutMapping
    public AjaxResult<Void> updateUserInfo(@RequestBody @Valid UserBasicInfoDTO userBasicInfoDTO){
        Long userId = UserContext.getUser();
        log.info("开始更改用户信息{}",userBasicInfoDTO);
        return AjaxResult.toAjax(userService.updateUserInfo(userId, userBasicInfoDTO));
    }

    @ApiOperation("修改密码")
    @PutMapping("/password")
    public AjaxResult updatePassword(@RequestBody @Valid PasswordDTO passwordDTO){
        Long userId = UserContext.getUser();
        return AjaxResult.toAjax(userService.updatePassword(passwordDTO));
    }


    @PutMapping("/money/deduct")
    @ApiOperation("扣减用户余额")
    public AjaxResult deductMoney(@RequestBody DeductLocalMoneyDTO deductLocalMoneyDTO){
        Long userId = UserContext.getUser();
        log.info("扣减用户{}余额的对象为:{}", userId, deductLocalMoneyDTO);
        userService.deductMoney(deductLocalMoneyDTO);
        return AjaxResult.success();
    }

    @GetMapping
    @ApiOperation("查询用户信息")
    public AjaxResult<UserVO> getUserInfo(){
        log.info("查询用户{}信息");
        UserVO userVO = userService.getUserInfo();
        return AjaxResult.success(userVO);
    }



}
