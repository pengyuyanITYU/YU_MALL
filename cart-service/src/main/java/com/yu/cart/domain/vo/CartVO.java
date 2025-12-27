package com.yu.cart.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "购物车VO实体")
public class CartVO {
    @ApiModelProperty("购物车条目id ")
    private Long id;
    @ApiModelProperty("sku商品id")
    private Long itemId;
    @ApiModelProperty("购买数量")
    private Integer num;
    @ApiModelProperty("商品标题")
    private String name;
    @ApiModelProperty("商品动态属性键值集")
    private String spec;
    @ApiModelProperty("价格,单位：分")
    private Long price;
    @ApiModelProperty("商品图片")
    private String image;

}
