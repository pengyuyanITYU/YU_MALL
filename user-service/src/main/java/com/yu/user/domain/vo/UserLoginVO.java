package com.yu.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "用户登录返回实体")
@Accessors(chain = true)
public class UserLoginVO implements Serializable {
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
    @ApiModelProperty(name = "用户等级积分")
    private Integer currentPoints;

    private String email;
    private Integer gender;
    @ApiModelProperty("生日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthday;

}
