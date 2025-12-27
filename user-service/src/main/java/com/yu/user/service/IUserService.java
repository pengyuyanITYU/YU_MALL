package com.yu.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.common.domain.AjaxResult;
import com.yu.user.domain.dto.*;
import com.yu.user.domain.po.User;
import com.yu.user.domain.vo.UserLoginVO;
import com.yu.user.domain.vo.UserRegisterVO;
import com.yu.user.domain.vo.UserVO;

import javax.validation.Valid;

public interface IUserService extends IService<User> {
    UserLoginVO login(LoginFormDTO loginFormDTO);

    UserRegisterVO register(RegisterFormDTO loginFormDTO);



    void deductMoney(DeductLocalMoneyDTO deductLocalMoneyDTO);

    boolean updateUserInfo(Long userId,  UserBasicInfoDTO userBasicInfoDTO);

    boolean updatePassword(@Valid PasswordDTO passwordDTO);

    UserVO getUserInfo();
}
