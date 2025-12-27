package com.yu.comment.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*; // 如果是Spring Boot 3，请使用 jakarta.validation.constraints.*
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户发表评价 DTO
 * </p>
 */
@Data
@ApiModel(value = "CommentDTO", description = "新增评价表单")
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品ID", required = true, example = "1001")
    @NotNull(message = "商品ID不能为空")
    private Long itemId;

    @ApiModelProperty(value = "SKU ID", example = "2005")
    private Long skuId;

    @ApiModelProperty(value = "订单ID", required = true, example = "123456789")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty(value = "订单明细ID", required = true, example = "556677")
    @NotNull(message = "订单明细ID不能为空")
    private Long orderDetailId;

    @ApiModelProperty(value = "评分(1-5)", required = true, example = "5")
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1分")
    @Max(value = 5, message = "评分最高为5分")
    private Integer rating;

    @ApiModelProperty(value = "评价内容", required = true, example = "东西很好，物流很快！")
    @NotBlank(message = "评价内容不能为空")
    @Size(max = 500, message = "评价内容不能超过500字")
    private String content;

    @ApiModelProperty(value = "图片列表(URL数组)")
    @Size(max = 9, message = "最多上传9张图片")
    private List<String> images;

    @ApiModelProperty(value = "是否匿名", example = "false")
    private Boolean isAnonymous;

    @ApiModelProperty(value = "用户名")
    private String userNickname;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;
}