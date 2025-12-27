package com.yu.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "会员等级实体")
public class MemberLevelVO implements Serializable {

    @ApiModelProperty("会员等级ID")
    private Long id;
    @ApiModelProperty("会员等级名称")
    private String levelName;
    @ApiModelProperty("会员等级积分下限")
    private Integer minPoints;
    @ApiModelProperty("会员等级积分上限")
    private Integer maxPoints;
    @ApiModelProperty("会员等级折扣")
    private String discountRate; // 格式化为百分比，如 "90%"
    @ApiModelProperty("会员等级创建时间")
    private Date createTime;
    @ApiModelProperty("会员等级更新时间")
    private Date updateTime;
}