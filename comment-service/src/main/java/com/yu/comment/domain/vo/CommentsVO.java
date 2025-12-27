package com.yu.comment.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评价列表展示 VO
 * </p>
 */
@Data
@ApiModel(description = "评价信息展示")
@Accessors(chain = true)
public class CommentsVO implements Serializable {

    @ApiModelProperty( "评价ID")
    private Long id;

    @ApiModelProperty( "商品ID")
    private Long itemId;

    @ApiModelProperty( "用户ID")
    private Long userId;

    private Long itemSkuId;

    @ApiModelProperty("用户昵称(若匿名则显示为***)")
    private String userNickname;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty( "评分")
    private Integer rating;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("图片列表")
    private List<String> images;

    @ApiModelProperty( "SKU信息(如: 红色, 64G)")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> skuSpecs; // 这是一个扩展字段，通常需要联表查询或在Service层填充

    @ApiModelProperty("点赞数")
    private Integer likeCount;

    @ApiModelProperty( "商家回复内容")
    private String merchantReplyContent;

    @ApiModelProperty("商家回复时间")
    private LocalDateTime merchantReplyTime;

    @ApiModelProperty( "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String itemName;

    private String itemImage;

}