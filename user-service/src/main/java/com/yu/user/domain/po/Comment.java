package com.yu.user.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comment")
public class Comment implements Serializable {

    /**
     * 评论id
    * */
    @TableId(value="id",type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
    * */
    private Long userId;

    /**
     * 商品ID
     * */
    private Long itemId;

    /**
     * 订单ID
     * */
    private Long orderId;

    /**
     * 评论内容
     * */
    private String content;

    /**
     * 评分
     * */
    private Byte score;

    /**
     * 创建时间
     * */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}