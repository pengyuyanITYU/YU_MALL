package com.yu.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "评论实体")
public class CommentDTO implements Serializable {
    @ApiModelProperty("评论人id")
    private Long userId;
    @ApiModelProperty("商品id")
    private Long itemId;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("评论内容")
    private String content;
    @ApiModelProperty("评分")
    private Byte score;
}