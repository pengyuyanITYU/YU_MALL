package com.yu.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(description = "用户注册返回实体")
@Accessors(chain = true)
public class UserRegisterVO implements Serializable {
    @ApiModelProperty(name = "用户登录token")
    private String token;
    @ApiModelProperty(name = "用户id")
    private Long userId;
    @ApiModelProperty(name = "用户名")
    private String username;
    @ApiModelProperty(name = "用户昵称")
    private String nickName;
    @ApiModelProperty(name = "用户头像")
    private String avatar;
    @ApiModelProperty(name = "用户余额")
    private Long balance;
    @ApiModelProperty(name = "用户等级名称")
    private String levelName;
    @ApiModelProperty(name = "用户当前积分")
    private Integer currentPoints;
}
