package com.yu.collect.domain.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CollectVO {

    private String name;

    private String tags;

    // 数据库是 int (分)，这里用 Integer 或 Long 均可，建议跟数据库保持一致
    private Integer price;

    private String image;

    private Long itemId;
}
