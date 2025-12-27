package com.yu.collect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.collect.domain.dto.CollectAddDTO;
import com.yu.collect.domain.entity.Collect;
import com.yu.collect.domain.vo.CollectVO;
import com.yu.collect.mapper.CollectMapper;
import com.yu.collect.service.ICollectService;
import com.yu.common.utils.BeanUtils;
import com.yu.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {

    @Override
    public void addCollect(CollectAddDTO dto) {
        Long userId = UserContext.getUser();
        if (userId == null){
            log.info("用户未登录");
        }
        Collect one = lambdaQuery().eq(Collect::getUserId, userId).eq(Collect::getItemId, dto.getItemId()).one();
        if(one!= null){
            if(one.getItemId() == dto.getItemId()){
                log.info("用户已收藏该商品");
                return;
            }
        }

        Collect collect = new Collect();
        collect.setUserId(userId)
                .setName(dto.getName())
                .setItemId(dto.getItemId())
                .setTags(dto.getTags())
                .setPrice(dto.getPrice())
                .setImage(dto.getImage());
        boolean save = save(collect);
        if(!save){
            log.info("收藏失败");
            throw new RuntimeException("收藏失败");
        }
    }

    @Override
    public List<CollectVO> getMyCollects() {
        Long userId = UserContext.getUser();
        List<Collect> list = lambdaQuery().eq(Collect::getUserId, userId).list();
        Set<CollectVO> collectVOSet = list.stream().map(collect -> {
            CollectVO collectVO = BeanUtils.copyBean(collect, CollectVO.class);
            return collectVO;
        }).collect(Collectors.toSet());
        return collectVOSet.stream().collect(Collectors.toList());
    }


    @Override
    public void cancelByItemId(Long id) {
        Long userId = UserContext.getUser();
        if(userId == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录");
        }
        lambdaUpdate().eq(Collect::getItemId, id).eq(Collect::getUserId, userId).remove();
    }
}