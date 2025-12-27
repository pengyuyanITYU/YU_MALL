package com.yu.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.comment.domain.po.CommentLikePO;


public interface ICommentLikeService extends IService<CommentLikePO> {
    void likeComment(Long id, Long itemId, Long skuId);

    Integer getByCommentId(Long id);

    void removeByCommentId(Long id);
}
