package com.yu.member.domain.dto;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@ApiModel(description = "会员等级保存/更新DTO")
@Data
public class MemberDTO {

    @NotBlank(message = "等级名称不能为空")
    @Size(max = 50, message = "等级名称最多50字符")
    @ApiModelProperty("会员等级名称")
    private String levelName;

    @NotNull(message = "最低积分不能为空")
    @Min(value = 0, message = "最低积分不能为负")
    @ApiModelProperty("该等级所需最低积分")
    private Integer minPoints;

    @NotNull(message = "最高积分不能为空")
    @ApiModelProperty("该等级最高积分，-1 表示无上限")
    private Integer maxPoints;

    @NotNull(message = "折扣率不能为空")
    @DecimalMin(value = "0.00", message = "折扣率最小为0")
    @DecimalMax(value = "1.00", message = "折扣率不能大于1")
    @Digits(integer = 1, fraction = 2, message = "折扣率格式错误，如0.95")
    @ApiModelProperty("折扣率，如0.95表示95折")
    private BigDecimal discountRate;
}
