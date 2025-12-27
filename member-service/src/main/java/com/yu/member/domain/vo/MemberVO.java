
package com.yu.member.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel(description = "会员等级返回VO")
@Data
public class MemberVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "等级名称，如 V1、黄金会员")
    private String levelName;

    @ApiModelProperty(value = "所需最低积分")
    private Integer minPoints;

    @ApiModelProperty(value = "最高积分，-1=无上限")
    private Integer maxPoints;

    @ApiModelProperty(value = "折扣率，如 0.95 表示 95 折")
    private BigDecimal discountRate;


}