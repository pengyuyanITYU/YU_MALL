package com.yu.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.comment.constants.LikeType;
import com.yu.comment.domain.po.CommentLikePO;
import com.yu.comment.mapper.CommentLikeMapper;
import com.yu.comment.service.ICommentLikeService;
import com.yu.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLikePO> implements ICommentLikeService {


    @Override
    public void likeComment(Long id, Long itemId, Long skuId) {
        log.info("点赞评论{}", id);
        Long userId = UserContext.getUser();
        CommentLikePO commentLikePO = new CommentLikePO();
        commentLikePO.setCommentId(id)
                     .setUserId(userId)
                     .setLikeType(LikeType.LIKE)
                     .setSkuId(skuId)
                     .setItemId(itemId);
        boolean save = save(commentLikePO);
        if (!save){
            log.error("点赞失败");
            throw new RuntimeException("点赞失败");
        }

    }

    @Override
    public Integer getByCommentId(Long id) {
        CommentLikePO one = lambdaQuery().eq(CommentLikePO::getCommentId, id).eq(CommentLikePO::getUserId, UserContext.getUser()).one();
        Integer likeType = one.getLikeType();
        if(LikeType.LIKE.equals(likeType)){
            return LikeType.LIKE;
        }else{
            return LikeType.UNLIKE;
        }

    }

    @Override
    public void removeByCommentId(Long id) {
        boolean remove = lambdaUpdate().eq(CommentLikePO::getCommentId, id).remove();
        if(!remove){
            log.error("取消赞失败");
            throw new RuntimeException("取消赞失败");
        }
    }
}
