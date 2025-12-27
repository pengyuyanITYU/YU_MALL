package com.yu.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("member_level")
public class MemberLevel implements Serializable {

    /**
     * 会员等级ID
     * */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会员等级名称
     * */
    private String levelName;

    /**
     * 当前会员等级最小积分
     * */
    private Integer minPoints;

    /**
     * 当前会员等级最大积分
     * */
    private Integer maxPoints;

    /**
     * 会员等级折扣
     * */
    private BigDecimal discountRate;

    /**
     * 创建时间
     * */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}