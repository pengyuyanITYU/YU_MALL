package com.yu.api.fallbacks;


import com.yu.api.client.ItemClient;
import com.yu.api.vo.ItemDetailVO;
import com.yu.common.domain.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ItemFallbackFactory implements FallbackFactory<ItemClient> {

    @Override
    public ItemClient create(Throwable cause) {
        return new ItemClient() {
            @Override
            public AjaxResult<ItemDetailVO> getItemById(Long id) {
                log.error("商品服务,根据ID查询商品失败", cause);
                return AjaxResult.error("商品服务,查询商品失败");
            }

            @Override
            public AjaxResult<List<ItemDetailVO>> getItemByIds(List<Long> ids) {
                log.error("商品服务,批量查询商品失败", cause);
                return AjaxResult.error("商品服务,批量查询商品失败");
            }
        };
    }
}
