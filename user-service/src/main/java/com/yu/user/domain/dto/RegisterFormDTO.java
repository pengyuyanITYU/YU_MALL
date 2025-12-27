package com.yu.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.Serializable;

@Data
@ApiModel("用户注册表单")
public class RegisterFormDTO implements Serializable {

    @ApiModelProperty(name="用户名", required = true)
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(name = "密码", required = true)
    @NotNull(message = "密码不能为空")
    private String password;

    @ApiModelProperty(name = "手机号", required = true)
    @NotNull(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(name = "头像", required = true)
    private String avatar;

    @ApiModelProperty(name = "昵称", required = true)
    @NotNull(message = "昵称不能为空")
    private String nickName;


}
