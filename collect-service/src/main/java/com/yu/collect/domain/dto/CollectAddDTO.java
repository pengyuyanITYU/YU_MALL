package com.yu.collect.domain.dto;

import lombok.Data;
import java.util.Map;

@Data
public class CollectAddDTO {
    private Long itemId;
    private String name;
    private String tags;
    private Integer price;
    private String image;
}