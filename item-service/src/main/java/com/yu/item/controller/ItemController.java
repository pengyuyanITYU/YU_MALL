package com.yu.item.controller;

import com.yu.common.domain.AjaxResult;
import com.yu.common.domain.page.TableDataInfo;
import com.yu.item.domain.query.ItemPageQuery;
import com.yu.item.domain.vo.ItemDetailVO;
import com.yu.item.service.IItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final IItemService itemService;

    @GetMapping("/list")
    @ApiOperation("查询商品列表")
    public TableDataInfo list(ItemPageQuery itemPageQuery) {
        log.info("开始执行查询商品列表{}", itemPageQuery);
        return itemService.listItem(itemPageQuery);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询商品详情")
    public AjaxResult<ItemDetailVO> getItemById(@PathVariable Long id) {
        log.info("开始执行查询商品详情{}", id);
        return AjaxResult.success(itemService.getItemById(id));
    }

    @GetMapping("/batch")
    @ApiOperation("批量查询商品详情")
    public AjaxResult<List<ItemDetailVO>> getItemByIds(@RequestParam("ids") List<Long> ids) {
        log.info("开始执行批量查询商品详情{}", ids);
        return AjaxResult.success(itemService.getItemByIds(ids));
    }

}
