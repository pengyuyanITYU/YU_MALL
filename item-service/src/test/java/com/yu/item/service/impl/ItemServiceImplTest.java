//package com.yu.item.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.yu.item.domain.po.Item;
//import com.yu.item.domain.po.ItemDetail;
//import com.yu.item.domain.po.ItemSku;
//import com.yu.item.domain.vo.ItemDetailVO;
//import com.yu.item.mapper.ItemDetailMapper;
//import com.yu.item.mapper.ItemMapper;
//import com.yu.item.mapper.ItemSkuMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//class ItemServiceImplTest {
//
//    @Mock
//    private ItemMapper itemMapper;
//
//    @Mock
//    private ItemDetailMapper itemDetailMapper;
//
//    @Mock
//    private ItemSkuMapper itemSkuMapper;
//
//    @InjectMocks
//    private ItemServiceImpl itemService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getItemById_ItemNotFound_ReturnNull() {
//        // 模拟商品不存在的情况
//        when(itemMapper.selectById(1L)).thenReturn(null);
//
//        // 执行测试方法
//        ItemDetailVO result = itemService.getItemById(1L);
//
//        // 验证结果
//        assertNull(result);
//    }
//
//    @Test
//    void getItemById_ItemFound_ReturnCompleteVO() {
//        // 1. 准备模拟数据
//        Item item = new Item();
//        item.setId(1L);
//        item.setName("Apple Watch S9 智能手表");
//        item.setSubTitle("S9芯片/双指互点两下/2000尼特亮度");
//        item.setPrice(2999L);
//        item.setOriginalPrice(3199);
//        item.setTags("热销,新品");
//        item.setSold(999);
//
//        ItemDetail itemDetail = new ItemDetail();
//        itemDetail.setItemId(1L);
//        itemDetail.setBannerImages("[\"https://example.com/img1.jpg\",\"https://example.com/img2.jpg\"]");
//        itemDetail.setDetailHtml("<div>商品详情HTML内容</div>");
//        itemDetail.setVideoUrl("https://example.com/video.mp4");
//        itemDetail.setSpecTemplate("[{\"key\": \"颜色\", \"values\": [\"午夜色\", \"星光色\", \"银色\"]}, {\"key\": \"版本\", \"values\": [\"41mm GPS版\", \"45mm GPS版\", \"41mm蜂窝版\"]}]");
//
//        ItemSku sku1 = new ItemSku();
//        sku1.setId(101L);
//        sku1.setItemId(1L);
//        sku1.setSpecs("{\"颜色\": \"午夜色\", \"版本\": \"41mm GPS版\"}");
//        sku1.setPrice(2999);
//        sku1.setStock(100);
//        sku1.setImage("https://example.com/sku1.jpg");
//
//        ItemSku sku2 = new ItemSku();
//        sku2.setId(102L);
//        sku2.setItemId(1L);
//        sku2.setSpecs("{\"颜色\": \"午夜色\", \"版本\": \"45mm GPS版\"}");
//        sku2.setPrice(3199);
//        sku2.setStock(50);
//        sku2.setImage("https://example.com/sku2.jpg");
//
//        // 2. 配置mock行为
//        when(itemMapper.selectById(1L)).thenReturn(item);
//        when(itemDetailMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(itemDetail);
//        when(itemSkuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Arrays.asList(sku1, sku2));
//
//        // 3. 执行测试方法
//        ItemDetailVO result = itemService.getItemById(1L);
//
//        // 4. 验证结果
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("Apple Watch S9 智能手表", result.getName());
//        assertEquals(2999, result.getPrice());
//        assertEquals(2, result.getBannerImages().size());
//        assertEquals("<div>商品详情HTML内容</div>", result.getDetailHtml());
//        assertEquals(2, result.getSpecTemplate().size());
//        assertEquals(2, result.getSkuList().size());
//
//        // 验证SKU信息
//        assertEquals(101L, result.getSkuList().get(0).getId());
//        assertEquals(2999, result.getSkuList().get(0).getPrice());
//        assertEquals("午夜色", result.getSkuList().get(0).getSpecs().get("颜色"));
//    }
//
//    @Test
//    void getItemById_WithoutDetailAndSku_ReturnBasicInfo() {
//        // 准备仅包含基本信息的商品
//        Item item = new Item();
//        item.setId(1L);
//        item.setName("Apple Watch S9");
//        item.setPrice(2999);
//
//        // 配置mock行为 - 无详情和SKU数据
//        when(itemMapper.selectById(1L)).thenReturn(item);
//        when(itemDetailMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
//        when(itemSkuMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(new ArrayList<>());
//
//        // 执行测试
//        ItemDetailVO result = itemService.getItemById(1L);
//
//        // 验证基本信息存在，其他信息为默认值或null
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//        assertEquals("Apple Watch S9", result.getName());
//        assertNull(result.getBannerImages());
//        assertNull(result.getDetailHtml());
//        assertTrue(result.getSkuList().isEmpty());
//    }
//}