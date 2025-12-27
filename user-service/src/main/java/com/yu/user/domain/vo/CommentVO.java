package com.yu.user.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "评论实体")
public class CommentVO implements Serializable {

    @ApiModelProperty("评论id")
    private Long id;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户名")
    private String username; // 用户名
    @ApiModelProperty("商品id")
    private Long itemId;
    @ApiModelProperty("商品名称")
    private String itemName; // 商品名称
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("评论内容")
    private String content;
    @ApiModelProperty("评分")
    private Byte score;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}