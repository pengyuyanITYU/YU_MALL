package com.yu.order.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;

@ApiModel(description = "订单明细条目(下单参数)")
@Data
@Accessors(chain = true)
public class OrderDetailDTO {

    @ApiModelProperty(value = "商品ID", required = true)
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    @ApiModelProperty(value = "购买数量", required = true)
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量不能少于1")
    private Integer num;

    @ApiModelProperty(value = "商品单价", required = true)
    @NotNull(message = "商品单价不能为空")
    @Min(value = 1, message = "商品单价不能少于1")
    private Long price;

    @ApiModelProperty(value = "商品图片", required = true)
    @NotNull(message = "商品图片不能为空")
    private String image;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotNull(message = "商品名称不能为空")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> specs;
//     注意：此处不包含 price/name/image，这些必须由后端查询
}