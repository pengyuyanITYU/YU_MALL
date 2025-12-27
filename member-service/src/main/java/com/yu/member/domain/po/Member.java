package com.yu.member.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("member_level")
public class Member {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 会员等级名称 */
    private String levelName;

    /** 最低积分 */
    private Integer minPoints;

    /** 最高积分（-1 表示无上限，建议数据库默认 -1） */
    private Integer maxPoints;

    /** 折扣率，0.90 表示 9 折 */
    @Digits(integer = 1, fraction = 2, message = "折扣率格式错误，如0.95")
    private BigDecimal discountRate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)

    private LocalDateTime updateTime;
}