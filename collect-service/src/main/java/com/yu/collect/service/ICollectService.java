package com.yu.collect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.collect.domain.dto.CollectAddDTO;
import com.yu.collect.domain.entity.Collect;
import com.yu.collect.domain.vo.CollectVO;

import java.util.List;

/**
 * <p>
 * 用户收藏表 服务类
 * </p>
 */
public interface ICollectService extends IService<Collect> {

    /**
     * 添加收藏
     *
     * @param dto 收藏添加参数
     * @return 是否添加成功
     */
    void addCollect(CollectAddDTO dto);

    /**
     * 获取当前用户的收藏列表
     *
     * @return 收藏列表
     */
    List<CollectVO> getMyCollects();

    void cancelByItemId(Long id);
}