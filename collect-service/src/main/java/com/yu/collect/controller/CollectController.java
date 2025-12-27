package com.yu.collect.controller;


import com.yu.collect.domain.dto.CollectAddDTO;
import com.yu.collect.domain.vo.CollectVO;
import com.yu.collect.service.ICollectService;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

@RestController
@RequestMapping("/collects")
@Slf4j
public class CollectController {

    @Autowired
    private ICollectService collectService;

    // 查询我的收藏
    @GetMapping
    public AjaxResult<List<CollectVO>> list() {
        log.info("查询我的收藏");
        return AjaxResult.success(collectService.getMyCollects());
    }

    // 添加收藏
    @PostMapping
    public AjaxResult add(@RequestBody CollectAddDTO dto) {
        collectService.addCollect(dto);
        return AjaxResult.success();
    }

    // 删除收藏 (支持单个或批量删除)
    @DeleteMapping
    public AjaxResult<Boolean> delete(@RequestParam List<Long> ids) {
        return AjaxResult.success(collectService.removeByIds(ids));
    }
    
    // 根据商品ID取消收藏 (商品详情页常用)
    @DeleteMapping("/{id}")
    @ApiImplicitParam(name = "id", value = "商品ID")
    public AjaxResult cancelByItemId(@PathVariable Long id) {
         // 逻辑略：根据 userId + itemId 删除
        collectService.cancelByItemId(id);
         return AjaxResult.success();
    }
}