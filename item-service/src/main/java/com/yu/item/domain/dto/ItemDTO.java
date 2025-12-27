package com.yu.item.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel(description = "商品发布/修改参数")
public class ItemDTO {

    @ApiModelProperty(value = "ID，新增时为空，修改时必填")
    private Long id;

    // --- SPU 基本信息 ---
    @ApiModelProperty(value = "商品名称", required = true)
    private String name;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "封面图")
    private String image;

    @ApiModelProperty(value = "默认价格(起步价)")
    private Long price;

    @ApiModelProperty(value = "原价")
    private Integer originalPrice;

    @ApiModelProperty(value = "标签，逗号分隔")
    private String tags;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "分类ID")
    private String category;

    @ApiModelProperty(value = "品牌ID")
    private String brand;

    // --- Detail 详情信息 ---
    @ApiModelProperty(value = "轮播图列表")
    private List<String> bannerImages;

    @ApiModelProperty(value = "详情信息")
    private String description;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "是否广告")
    private Boolean isAd;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private String updateTime;
}