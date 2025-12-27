package com.yu.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("修改密码参数")
public class PasswordDTO {

    @ApiModelProperty("手机号")
    @NotNull(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("旧密码")
    @NotNull(message = "旧密码不能为空")
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotNull(message = "新密码不能为空")
    private String newPassword;

    @ApiModelProperty("确认密码")
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;
}
