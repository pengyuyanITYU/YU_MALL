package com.yu.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "会员等级实体")
public class MemberLevelDTO implements Serializable {

    @ApiModelProperty("会员等级")
    private String levelName;
    @ApiModelProperty("会员等级对应的最小积分")
    private Integer minPoints;
    @ApiModelProperty("会员等级对应的最大积分")
    private Integer maxPoints;
    @ApiModelProperty("会员等级对应的折扣率")
    private Double discountRate;
}