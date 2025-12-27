package com.yu.api.client;

import com.yu.api.fallbacks.ItemFallbackFactory;
import com.yu.api.vo.ItemDetailVO;
import com.yu.common.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "yu-mall-item-service", path = "/items", fallbackFactory = ItemFallbackFactory.class)
public interface ItemClient {

    @GetMapping("/{id}")
    AjaxResult<ItemDetailVO> getItemById(@PathVariable("id") Long id);

    @GetMapping("/batch")
    AjaxResult<List<ItemDetailVO>> getItemByIds(@RequestParam List<Long> ids);

}
