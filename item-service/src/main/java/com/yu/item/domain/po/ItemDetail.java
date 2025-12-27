package com.yu.item.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("item_detail")
public class ItemDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long itemId;

    // JSON字段在PO中建议先用String接收，或配合MyBatis TypeHandler
    private String bannerImages; // ["url1", "url2"]
    private String detailHtml;   // 富文本
    private String specTemplate; // [{"name":"颜色","values":["红","黑"]}]
    private String videoUrl;
}