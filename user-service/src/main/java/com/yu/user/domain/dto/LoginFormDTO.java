package com.yu.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(description = "用户登录参数")
public class LoginFormDTO implements Serializable {

    @ApiModelProperty(name = "用户名",required= true)
    @NotNull(message = "用户名不能为空")
    private String username;
    @ApiModelProperty(name = "密码")
    @NotNull(message = "密码不能为空")
    private String password;
    @ApiModelProperty(name = "是否记住我", required = false)
    private Boolean rememberMe = false;
}
