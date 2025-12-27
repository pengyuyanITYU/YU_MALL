package com.yu.comment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "item_comment", autoResultMap = true) // autoResultMap 必须开启，否则 JSON 无法自动转换
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 具体规格ID(SKU)，如果商品无规格可为空
     */
    private Long skuId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long orderDetailId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称(冗余字段)
     */
    private String userNickname;

    /**
     * 用户头像(冗余字段)
     */
    private String userAvatar;

    /**
     * 评分星级(1-5)
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片/视频列表
     * 数据库为 json 类型，自动映射为 List<String>
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> images;

    /**
     * 是否匿名(0:否 1:是)
     */
    private Boolean isAnonymous;

    /**
     * 审核状态(0:待审核 1:审核通过 2:审核拒绝/隐藏)
     */
    private Integer status;

    /**
     * 是否置顶(0:否 1:是)
     */
    private Boolean isTop;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 回复数(追评数)
     */
    private Integer replyCount;

    /**
     * 商家回复内容
     */
    private String merchantReplyContent;

    /**
     * 商家回复时间
     */
    private LocalDateTime merchantReplyTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除(0:正常 1:删除)
     */
//    @TableLogic
//    private Boolean deleted;

}