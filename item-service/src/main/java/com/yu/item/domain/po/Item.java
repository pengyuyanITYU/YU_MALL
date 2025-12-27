package com.yu.item.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("item")
@EqualsAndHashCode(callSuper = false)
public class Item {
    @TableId(type = IdType.AUTO)
    private Long id;

    // 基础信息
    private String name;
    private String subTitle; // 副标题
    private String image;    // 列表图

    // 价格通常存为分(Integer)或直接存Decimal，这里对应数据库int
    private Long price;
    private Integer originalPrice; // 原价

    // 库存与销量
    private Integer stock;
    private Integer sold;

    // 状态与标签
    private Integer commentCount;
    private BigDecimal avgScore;
    private String tags;   // "包邮,满减"
    private Integer status; // 1上架 2下架

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String category;

    private String brand;
}