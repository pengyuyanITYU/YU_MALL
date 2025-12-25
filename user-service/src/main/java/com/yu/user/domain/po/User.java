package com.yu.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yu.user.enums.UserStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码，加密存储
     */
    @ApiModelProperty("密码，加密存储")
    private String password;

    /**
     * 注册手机号
     */
    @ApiModelProperty("注册手机号")
    private String phone;

    /**
     * 创建时间
     */

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 使用状态（1正常 2冻结）
     */
    @ApiModelProperty("使用状态")
    private UserStatus status;

    /**
     * 账户余额
     */
    @ApiModelProperty("账户余额")
    private Long balance;

    /**
    * 用户头像
    * */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
    * 用户昵称
    * */
    @ApiModelProperty("用户昵称")
    private String nickName;

    /**
     * 会员等级ID
    * */
    @ApiModelProperty("会员等级ID")
    private String levelName;

    /**
     * 支付密码
     * */
    @ApiModelProperty("支付密码")
    private String payPassword;

    /*
    * 当前会员积分
    * */
    @ApiModelProperty("当前会员积分")
    private Integer currentPoints;

    @ApiModelProperty("会员等级ID")
    private Integer memberLevelId;

    private String email;

    private Integer gender;

    @ApiModelProperty("生日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthday;

}
