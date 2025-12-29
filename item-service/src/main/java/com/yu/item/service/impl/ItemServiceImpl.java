package com.yu.item.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yu.api.client.OrderClient;
import com.yu.common.constant.HttpStatus;
import com.yu.common.domain.page.TableDataInfo;
import com.yu.common.exception.BusinessException;
import com.yu.common.utils.BeanUtils;
import com.yu.common.utils.CollUtils;
import com.yu.common.utils.UserContext;
import com.yu.item.domain.dto.OrderDetailDTO;
import com.yu.item.domain.po.Item;
import com.yu.item.domain.po.ItemDetail;
import com.yu.item.domain.po.ItemSku;
import com.yu.item.domain.query.ItemPageQuery;
import com.yu.item.domain.vo.ItemDetailVO;
import com.yu.item.mapper.ItemDetailMapper;
import com.yu.item.mapper.ItemMapper;
import com.yu.item.mapper.ItemSkuMapper;
import com.yu.item.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl extends  ServiceImpl<ItemMapper, Item> implements IItemService{

    private final ItemMapper itemMapper;
    private final ItemDetailMapper itemDetailMapper;
    private final ItemSkuMapper itemSkuMapper;
    private final OrderClient orderClient;

    @Override
    public void updateStockAndSold(List<OrderDetailDTO>  orderDetailDTOList) {
        List<Item> items = new ArrayList<>();
        Long user = UserContext.getUser();
        orderDetailDTOList.forEach(orderDetailDTO -> {
            Item item = lambdaQuery().eq(Item::getId, orderDetailDTO.getItemId())
                    .eq(Item::getStatus, 1)
                    .one();
            if (item != null) {
                item.setSold(item.getSold() + orderDetailDTO.getNum());
                item.setStock(item.getStock() - orderDetailDTO.getNum());
                items.add(item);
            }
        });
        boolean b = updateBatchById(items);
        if (!b) {
            log.error("更新库存失败");
            throw new BusinessException("更新库存失败");
        }
    }

    @Override
    public TableDataInfo listItem(ItemPageQuery itemPageQuery) {
        Page<Item> p = new Page<>(itemPageQuery.getPageNo(), itemPageQuery.getPageSize());
        OrderItem orderItem = new OrderItem();
        OrderItem orderItem1 = new OrderItem();
        if (itemPageQuery.getSortBy() != null){
            orderItem.setColumn(itemPageQuery.getSortBy());
        }else{
            orderItem.setColumn("create_time");
        }
        if(itemPageQuery.getIsAsc() !=  null){
            orderItem.setAsc(itemPageQuery.getIsAsc());
        }else{
            orderItem.setAsc(false);
        }
        if(StrUtil.isNotBlank(itemPageQuery.getSold())){
            orderItem1.setColumn("sold");
            orderItem1.setAsc(false);
            p.addOrder(orderItem1);
        }
        p.addOrder(orderItem);
        Page<Item> result = lambdaQuery().eq(Item::getStatus, 1)

                .like(StrUtil.isNotBlank(itemPageQuery.getName()), Item::getName, itemPageQuery.getName())
                .ge(itemPageQuery.getMinPrice() != null, Item::getPrice, itemPageQuery.getMinPrice())
                .le(itemPageQuery.getMaxPrice() != null, Item::getPrice, itemPageQuery.getMaxPrice())
                .eq(StrUtil.isNotBlank(itemPageQuery.getCategory()), Item::getCategory, itemPageQuery.getCategory())
                .page(p);
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setMsg("查询成功");
        tableDataInfo.setRows(result.getRecords());
        tableDataInfo.setTotal(result.getTotal());
        return tableDataInfo;}



    @Override
    public ItemDetailVO getItemById(Long id) {
        // 1. 查询商品基本信息
        Item item = itemMapper.selectById(id);
        if (item == null) {
            return null;
        }
        
        // 2. 查询商品详情信息
        LambdaQueryWrapper<ItemDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(ItemDetail::getItemId, id);
        ItemDetail itemDetail = itemDetailMapper.selectOne(detailWrapper);
        
        // 3. 查询商品SKU列表
        LambdaQueryWrapper<ItemSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ItemSku::getItemId, id);
        List<ItemSku> skuList = itemSkuMapper.selectList(skuWrapper);
        
        // 4. 组装ItemDetailVO
        ItemDetailVO itemDetailVO = new ItemDetailVO();
        
        // 设置商品基本信息
        itemDetailVO.setId(item.getId());
        itemDetailVO.setName(item.getName());
        itemDetailVO.setSubTitle(item.getSubTitle());
        itemDetailVO.setPrice(item.getPrice());
        itemDetailVO.setOriginalPrice(item.getOriginalPrice());
        itemDetailVO.setTags(item.getTags());
        itemDetailVO.setSold(item.getSold());
        itemDetailVO.setAvgScore(item.getAvgScore());
        
        // 设置商品详情信息
        if (itemDetail != null) {
            // 将JSON字符串转换为Java对象
            if (itemDetail.getBannerImages() != null) {
                itemDetailVO.setBannerImages(JSON.parseArray(itemDetail.getBannerImages(), String.class));
            }
            itemDetailVO.setDetailHtml(itemDetail.getDetailHtml());
            itemDetailVO.setVideoUrl(itemDetail.getVideoUrl());
            
            // 解析规格模板
            if (itemDetail.getSpecTemplate() != null) {
                itemDetailVO.setSpecTemplate(JSON.parseArray(itemDetail.getSpecTemplate(), ItemDetailVO.SpecTemplateItem.class));
            }
        }
        
        // 设置SKU列表
        if (!skuList.isEmpty()) {
            List<ItemDetailVO.SkuVO> skuVOS = skuList.stream().map(sku -> {
                ItemDetailVO.SkuVO skuVO = new ItemDetailVO.SkuVO();
                skuVO.setId(sku.getId());
                skuVO.setPrice(sku.getPrice());
                skuVO.setStock(sku.getStock());
                skuVO.setImage(sku.getImage());
                
                // 将规格JSON字符串转换为Map
                if (sku.getSpecs() != null) {
                    skuVO.setSpecs(JSON.parseObject(sku.getSpecs(), Map.class));
                }
                
                return skuVO;
            }).collect(java.util.stream.Collectors.toList());
            
            itemDetailVO.setSkuList(skuVOS);
        }
        
        return itemDetailVO;
    }


    @Override
    public List<ItemDetailVO> getItemByIds(List<Long> ids) {
        // 0. 判空处理
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 批量查询商品基本信息
        List<Item> items = listByIds(ids);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        // 确保后续查询只查存在的ID
        List<Long> validIds = items.stream().map(Item::getId).collect(Collectors.toList());

        // 2. 批量查询商品详情信息 (WHERE item_id IN (...))
        LambdaQueryWrapper<ItemDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.in(ItemDetail::getItemId, validIds);
        List<ItemDetail> detailList = itemDetailMapper.selectList(detailWrapper);

        // 转为 Map<ItemId, ItemDetail> 方便后续 O(1) 获取
        Map<Long, ItemDetail> detailMap = detailList.stream()
                .collect(Collectors.toMap(ItemDetail::getItemId, Function.identity()));

        // 3. 批量查询商品SKU列表 (WHERE item_id IN (...))
        LambdaQueryWrapper<ItemSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.in(ItemSku::getItemId, validIds);
        List<ItemSku> allSkuList = itemSkuMapper.selectList(skuWrapper);

        // 转为 Map<ItemId, List<ItemSku>> 进行分组
        Map<Long, List<ItemSku>> skuMap = allSkuList.stream()
                .collect(Collectors.groupingBy(ItemSku::getItemId));

        // 4. 组装结果 List
        return items.stream().map(item -> {
            ItemDetailVO itemDetailVO = new ItemDetailVO();

            // --- A. 设置商品基本信息 ---
            // 可以使用 BeanUtils 简化，也可以像参考代码一样手动 set
            itemDetailVO.setId(item.getId());
            itemDetailVO.setName(item.getName());
            itemDetailVO.setSubTitle(item.getSubTitle());
            itemDetailVO.setPrice(item.getPrice());
            itemDetailVO.setOriginalPrice(item.getOriginalPrice());
            itemDetailVO.setTags(item.getTags());
            itemDetailVO.setSold(item.getSold());
            itemDetailVO.setAvgScore(item.getAvgScore());

            // --- B. 设置商品详情信息 ---
            ItemDetail itemDetail = detailMap.get(item.getId());
            if (itemDetail != null) {
                if (itemDetail.getBannerImages() != null) {
                    itemDetailVO.setBannerImages(JSON.parseArray(itemDetail.getBannerImages(), String.class));
                }
                itemDetailVO.setDetailHtml(itemDetail.getDetailHtml());
                itemDetailVO.setVideoUrl(itemDetail.getVideoUrl());

                if (itemDetail.getSpecTemplate() != null) {
                    itemDetailVO.setSpecTemplate(JSON.parseArray(itemDetail.getSpecTemplate(), ItemDetailVO.SpecTemplateItem.class));
                }
            }

            // --- C. 设置SKU列表 ---
            List<ItemSku> currentItemSkus = skuMap.get(item.getId());
            if (currentItemSkus != null && !currentItemSkus.isEmpty()) {
                List<ItemDetailVO.SkuVO> skuVOS = currentItemSkus.stream().map(sku -> {
                    ItemDetailVO.SkuVO skuVO = new ItemDetailVO.SkuVO();
                    skuVO.setId(sku.getId());
                    skuVO.setPrice(sku.getPrice());
                    skuVO.setStock(sku.getStock());
                    skuVO.setImage(sku.getImage());

                    if (sku.getSpecs() != null) {
                        skuVO.setSpecs(JSON.parseObject(sku.getSpecs(), Map.class));
                    }
                    return skuVO;
                }).collect(Collectors.toList());

                itemDetailVO.setSkuList(skuVOS);
            }

            return itemDetailVO;
        }).collect(Collectors.toList());
    }
}
