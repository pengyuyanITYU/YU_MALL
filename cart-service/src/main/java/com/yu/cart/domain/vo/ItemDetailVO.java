package com.yu.cart.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ItemDetailVO {
    // --- 来自 item 表 ---
    private Long id;
    private String name;
    private String subTitle;
    private Long price;          // 当前显示价格(最低价)
    private Long originalPrice;  // 划线价
    private String tags;            // 标签
    private Integer sold;           // 销量
    private BigDecimal avgScore;    // 评分

    // --- 来自 item_detail 表 ---
    // 将数据库的 String JSON 转为 List
    private List<String> bannerImages; 
    private String videoUrl;
    private String detailHtml;

    // 核心：规格选择器模板
    // 前端根据这个渲染按钮：颜色[午夜色, 星光色], 版本[41mm, 45mm]
    private List<SpecTemplateItem> specTemplate; 

    // --- 来自 item_sku 表 ---
    // 所有SKU的列表，前端拿到后用于匹配价格和库存
    private List<SkuVO> skuList;

    // --- 内部静态类定义结构 ---
    
    /**
     * 规格模板项
     * 例如: name="选择颜色", values=["午夜色", "星光色"]
     */
    @Data
    public static class SpecTemplateItem {
        private String name;
        private List<String> values;
    }

    /**
     * 单个SKU信息
     */
    @Data
    public static class SkuVO {
        private Long id;
        // Map对应数据库的 specs JSON: {"颜色":"午夜色", "版本":"41mm"}
        private Map<String, String> specs; 
        private Long price;
        private Integer stock;
        private String image;
    }
}