package com.yu.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.sf.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel("用户信息")
public class UserBasicInfoDTO {

    @ApiModelProperty("昵称")
    @NotNull(message = "昵称不能为空")
    private String nickName;

    @ApiModelProperty("头像")
    @NotNull(message = "头像不能为空")
    private String avatar;

    @ApiModelProperty("手机号")
    @NotNull(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别")
    @Max(value = 1, message = "性别只能是一位数字")
    private Integer gender;

    @ApiModelProperty("生日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthday;

}
