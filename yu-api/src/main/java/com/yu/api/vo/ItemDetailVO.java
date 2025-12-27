package com.yu.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "商品详情")
public class ItemDetailVO {
    // --- 来自 item 表 ---
    @ApiModelProperty(value = "商品id")
    private Long id;
    @ApiModelProperty(value = "商品名称")
    private String name;
    @ApiModelProperty(value = "商品副标题")
    private String subTitle;
    @ApiModelProperty(value = "商品价格")
    private Long price;          // 当前显示价格(最低价)
    @ApiModelProperty(value = "商品原价")
    private Long originalPrice;  // 划线价
    @ApiModelProperty(value = "商品标签")
    private String tags;            // 标签
    @ApiModelProperty(value = "商品销量")
    private Integer sold;           // 销量
    @ApiModelProperty(value = "商品评分")
    private BigDecimal avgScore;    // 评分

    // --- 来自 item_detail 表 ---
    // 将数据库的 String JSON 转为 List
    @ApiModelProperty(value = "商品轮播图")
    private List<String> bannerImages;
    @ApiModelProperty(value = "商品视频")
    private String videoUrl;
    @ApiModelProperty(value = "商品详情html")
    private String detailHtml;

    // 核心：规格选择器模板
    // 前端根据这个渲染按钮：颜色[午夜色, 星光色], 版本[41mm, 45mm]
    @ApiModelProperty(value = "商品规格选择器模板")
    private List<SpecTemplateItem> specTemplate; 

    // --- 来自 item_sku 表 ---
    // 所有SKU的列表，前端拿到后用于匹配价格和库存
    @ApiModelProperty(value = "商品SKU列表")
    private List<SkuVO> skuList;

    // --- 内部静态类定义结构 ---
    
    /**
     * 规格模板项
     * 例如: name="选择颜色", values=["午夜色", "星光色"]
     */
    @Data
    public static class SpecTemplateItem {
        @ApiModelProperty(value = "规格模板项名称")
        private String name;
        @ApiModelProperty(value = "规格模板项值列表")
        private List<String> values;
    }

    /**
     * 单个SKU信息
     */
    @Data
    public static class SkuVO {
        @ApiModelProperty(value = "商品SKU id")
        private Long id;
        // Map对应数据库的 specs JSON: {"颜色":"午夜色", "版本":"41mm"}
        @ApiModelProperty(value = "商品SKU规格")
        private Map<String, String> specs;
        @ApiModelProperty(value = "商品SKU价格")
        private Long price;
        @ApiModelProperty(value = "商品SKU库存")
        private Integer stock;
        @ApiModelProperty(value = "商品SKU图片")
        private String image;
    }
}