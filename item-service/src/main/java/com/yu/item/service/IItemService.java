package com.yu.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.common.domain.page.TableDataInfo;
import com.yu.item.domain.dto.OrderDetailDTO;
import com.yu.item.domain.po.Item;
import com.yu.item.domain.query.ItemPageQuery;
import com.yu.item.domain.vo.ItemDetailVO;

import java.util.List;

public interface IItemService extends IService<Item> {


    TableDataInfo listItem(ItemPageQuery itemPageQuery);

    ItemDetailVO getItemById(Long id);

    List<ItemDetailVO> getItemByIds(List<Long> ids);

    void updateStockAndSold(List<OrderDetailDTO>  orderDetailDTOList);
}
