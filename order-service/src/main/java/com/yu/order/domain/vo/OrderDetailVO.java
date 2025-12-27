package com.yu.order.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@ApiModel(description = "订单商品详情视图")
@Accessors(chain = true)
public class OrderDetailVO {

    @ApiModelProperty("详情ID")
    private Long id;

    @ApiModelProperty("商品ID")
    private Long itemId;

    @ApiModelProperty("购买数量")
    private Integer num;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品规格(JSON)")
    private Map<String, String> spec;

    @ApiModelProperty("购买时的单价(分)")
    private Long price;

    @ApiModelProperty("商品图片URL")
    private String image;

    @ApiModelProperty("是否评价")
    private boolean commented;
}