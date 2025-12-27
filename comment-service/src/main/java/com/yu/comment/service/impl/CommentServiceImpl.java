package com.yu.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.api.client.ItemClient;
import com.yu.api.client.OrderClient;
import com.yu.api.client.UserClient;
import com.yu.api.vo.ItemDetailVO;
import com.yu.comment.constants.LikeType;
import com.yu.comment.domain.dto.CommentDTO;
import com.yu.comment.domain.po.Comment;
import com.yu.comment.domain.vo.CommentsVO;
import com.yu.comment.domain.vo.CommentVO;
import com.yu.comment.mapper.CommentMapper;
import com.yu.comment.service.ICommentLikeService;
import com.yu.comment.service.ICommentService;
import com.yu.common.domain.AjaxResult;
import com.yu.common.utils.BeanUtils;
import com.yu.common.utils.CollUtils;
import com.yu.common.utils.UserContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    private final ICommentLikeService commentLikeService;

    private final UserClient userClient;

    private final OrderClient orderClient;

    private final ItemClient  itemClient;

    @Override
    public List<CommentsVO> listComments(Long id) {
        List<Comment> list = lambdaQuery().eq(id != null, Comment::getItemId, id).list();
        if (CollUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        // 2. 准备 ItemId 集合
        Set<Long> itemIds = list.stream().map(Comment::getItemId).collect(Collectors.toSet());
        if (CollUtils.isEmpty(itemIds)) {
            return BeanUtils.copyToList(list, CommentsVO.class);
        }

        // 3. 远程查询商品详情 & 转 Map
        AjaxResult<List<ItemDetailVO>> itemDatas = itemClient.getItemByIds(new ArrayList<>(itemIds));
        Map<Long, ItemDetailVO> itemMap;

        if (itemDatas.isSuccess() && CollUtils.isNotEmpty(itemDatas.getData())) {
            itemMap = itemDatas.getData().stream()
                    .collect(Collectors.toMap(ItemDetailVO::getId, Function.identity(), (k1, k2) -> k1));
        } else {
            itemMap = Collections.emptyMap();
        }

        // 4. 【核心修改】遍历原始 Comment List，直接组装 VO
        Map<Long, ItemDetailVO> finalItemMap = itemMap; // lambda需要final变量
        return list.stream().map(comment -> {
            // A. 先复制基础属性 (内容、时间、评分等)
            CommentsVO vo = BeanUtils.copyBean(comment, CommentsVO.class);

            // B. 获取商品详情
            ItemDetailVO itemDetail = finalItemMap.get(comment.getItemId());
            if (itemDetail != null) {
                vo.setItemName(itemDetail.getName());

                // C. 【重点】用原始 comment.getSkuId() 去匹配 SKU
                if (CollUtils.isNotEmpty(itemDetail.getSkuList())) {
                    // 寻找匹配的 SKU
                    ItemDetailVO.SkuVO targetSku = itemDetail.getSkuList().stream()
                            .filter(sku -> sku.getId().equals(comment.getSkuId())) // 这里用的是 comment 里的 ID！
                            .findFirst()
                            .orElse(null);

                    if (targetSku != null) {
                        // 找到了：用 SKU 的图和规格
                        vo.setItemImage(targetSku.getImage());
                        vo.setSkuSpecs(targetSku.getSpecs());
                    } else {
                        // 没找到 (可能数据不一致)：兜底用列表第一个
                        vo.setItemImage(itemDetail.getSkuList().get(0).getImage());
                        vo.setSkuSpecs(itemDetail.getSkuList().get(0).getSpecs());
                    }
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void comment(CommentDTO commentDTO) {
        Long userId = UserContext.getUser();
        log.info("用户{}发表评论{}", userId, commentDTO);
        Comment comment = BeanUtils.copyBean(commentDTO, Comment.class);
        if(comment.getIsAnonymous() ){
            comment.setUserNickname("匿名用户");
        }else{
            comment.setUserNickname(commentDTO.getUserNickname());
        }
        comment.setUserId(userId);
        comment.setUserAvatar(commentDTO.getUserAvatar());
        boolean save = save(comment);
        if (!save){
            log.error("添加评论失败");
            throw new RuntimeException("添加评论失败");
        }
        com.yu.api.dto.CommentDTO commentDto = BeanUtils.copyBean(commentDTO, com.yu.api.dto.CommentDTO.class);
        orderClient.updateOrderCommented(commentDto);
        log.info("更新订单{}状态为已评价成功", comment.getOrderId());
    }

    @Override
    public void deleteMyComment(Long id) {
        Long userId = UserContext.getUser();
        if(userId == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录");
        }
        Comment comment = lambdaQuery().eq(Comment::getUserId, userId).eq(Comment::getId, id).one();
        if(comment == null){
          return;
        }
        removeById(id);
    }

    @Override
    public List<CommentsVO> getMyComments() {
        // 1. 查数据库 (Source of Truth)
        List<Comment> list = lambdaQuery().eq(Comment::getUserId, UserContext.getUser()).list();
        if (CollUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        // 2. 准备 ItemId 集合
        Set<Long> itemIds = list.stream().map(Comment::getItemId).collect(Collectors.toSet());
        if (CollUtils.isEmpty(itemIds)) {
            return BeanUtils.copyToList(list, CommentsVO.class);
        }

        // 3. 远程查询商品详情 & 转 Map
        AjaxResult<List<ItemDetailVO>> itemDatas = itemClient.getItemByIds(new ArrayList<>(itemIds));
        Map<Long, ItemDetailVO> itemMap;

        if (itemDatas.isSuccess() && CollUtils.isNotEmpty(itemDatas.getData())) {
            itemMap = itemDatas.getData().stream()
                    .collect(Collectors.toMap(ItemDetailVO::getId, Function.identity(), (k1, k2) -> k1));
        } else {
            itemMap = Collections.emptyMap();
        }

        // 4. 【核心修改】遍历原始 Comment List，直接组装 VO
        Map<Long, ItemDetailVO> finalItemMap = itemMap; // lambda需要final变量
        return list.stream().map(comment -> {
            // A. 先复制基础属性 (内容、时间、评分等)
            CommentsVO vo = BeanUtils.copyBean(comment, CommentsVO.class);

            // B. 获取商品详情
            ItemDetailVO itemDetail = finalItemMap.get(comment.getItemId());
            if (itemDetail != null) {
                vo.setItemName(itemDetail.getName());

                // C. 【重点】用原始 comment.getSkuId() 去匹配 SKU
                if (CollUtils.isNotEmpty(itemDetail.getSkuList())) {
                    // 寻找匹配的 SKU
                    ItemDetailVO.SkuVO targetSku = itemDetail.getSkuList().stream()
                            .filter(sku -> sku.getId().equals(comment.getSkuId())) // 这里用的是 comment 里的 ID！
                            .findFirst()
                            .orElse(null);

                    if (targetSku != null) {
                        // 找到了：用 SKU 的图和规格
                        vo.setItemImage(targetSku.getImage());
                        vo.setSkuSpecs(targetSku.getSpecs());
                    } else {
                        // 没找到 (可能数据不一致)：兜底用列表第一个
                        vo.setItemImage(itemDetail.getSkuList().get(0).getImage());
                        vo.setSkuSpecs(itemDetail.getSkuList().get(0).getSpecs());
                    }
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void likeComment(Long id) {
        Long userId = UserContext.getUser();
        if(userId == null){
            log.error("用户未登录");
            throw new RuntimeException("用户未登录");
        }
        Integer likeType = commentLikeService.getByCommentId(id);
        Comment comment = lambdaQuery().eq(Comment::getId, id).one();
        if (Objects.equals(likeType, LikeType.LIKE)){
            commentLikeService.removeByCommentId(id);
            boolean update = lambdaUpdate().eq(Comment::getId, id).setSql("like_count = like_count - 1") .update();
            if (!update){
                log.error("更新item_comment取消点赞失败");
                throw new RuntimeException("取消点赞失败");
            }
            return;
        }
        commentLikeService.likeComment(id,comment.getItemId(),comment.getSkuId());
        boolean update = lambdaUpdate().eq(Comment::getId, id) .setSql("like_count = like_count + 1") .update();
        if (!update){
            log.error("点赞失败");
            throw new RuntimeException("点赞失败");
        }
    }

    @Override
    public CommentVO getUserCommentDetail(Long id) {

        Comment comment = lambdaQuery().eq(id != null,Comment::getId, id).one();
        if (comment == null){
            log.error("id为null或者评论不存在");
            throw new RuntimeException("id为null或者评论不存在");
        }
        CommentVO commentVO = BeanUtils.copyBean(comment, CommentVO.class);
        return commentVO;
    }
}
