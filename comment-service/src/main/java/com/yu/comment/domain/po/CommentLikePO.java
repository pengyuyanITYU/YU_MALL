package com.yu.comment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.yu.comment.constants.LikeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@TableName("item_comment_like")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommentLikePO{

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 评价ID（对应 item_comment.id） */

    private Long commentId;

    /** 点赞用户ID */

    private Long userId;

    /** 点赞类型：1=点赞 2=取消赞 */

    private Integer likeType;

    /** 冗余字段：商品ID（用于统计热点商品点赞） */

    private Long itemId;

    /** 冗余字段：SKU ID */

    private Long skuId;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}