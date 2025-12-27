
package com.yu.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@ApiModel(description = "会员等级返回VO")
@EqualsAndHashCode(callSuper = false)
@Data
public class MemberVO {

    @ApiModelProperty(value = "会员等级ID")
    private Integer id;

    @ApiModelProperty(value = "等级名称，如 V1、黄金会员")
    private String levelName;

    @ApiModelProperty(value = "所需最低积分")
    private Integer minPoints;

    @ApiModelProperty(value = "最高积分，-1=无上限")
    private Integer maxPoints;

    @ApiModelProperty(value = "折扣率，如 0.95 表示 95 折")
    private BigDecimal discountRate;


}