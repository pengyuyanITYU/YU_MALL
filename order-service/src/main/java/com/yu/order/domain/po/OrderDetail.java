package com.yu.order.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value ="order_detail", autoResultMap = true)
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单详情id (自增)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * sku商品id
     */
    private Long itemId;

    /**
     * 购买数量
     */
    private Integer num;

    // ==========================================
    // 商品快照 (即使商品修改/下架，订单信息也不变)
    // ==========================================

    /**
     * 商品标题
     */
    private String name;

    /**
     * 商品动态属性键值集 (JSON)
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> spec;

    /**
     * 成交单价,单位：分
     */
    private Long price;

    /**
     * 商品图片
     */
    private String image;

    // ==========================================
    // 基础字段
    // ==========================================

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private boolean commented;

    public boolean isCommented() {
        return commented;
    }

    public  void setCommented(boolean commented) {
        this.commented = commented;
    }
}