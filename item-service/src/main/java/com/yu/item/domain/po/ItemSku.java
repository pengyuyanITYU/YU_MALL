package com.yu.item.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("item_sku")
public class ItemSku {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long itemId;

    // {"颜色": "午夜色", "版本": "41mm GPS版"}
    private String specs; 

    private Long price;
    private Integer stock;
    private String image; // 该SKU对应的特定图片
}