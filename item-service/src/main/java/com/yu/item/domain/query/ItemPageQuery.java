package com.yu.item.domain.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "商品分页查询参数")
public class ItemPageQuery extends PageQuery {
    @ApiModelProperty(name = "搜索关键字")
    private String name;
    private String category;
    @ApiModelProperty(name = "商品品牌")
    private String brand;
    @ApiModelProperty(name = "价格最小值")
    private Integer minPrice;
    @ApiModelProperty(name = "价格最大值")
    private Integer maxPrice;
}
