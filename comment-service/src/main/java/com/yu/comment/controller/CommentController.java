package com.yu.comment.controller;


import com.yu.comment.domain.dto.CommentDTO;
import com.yu.comment.domain.vo.CommentsVO;
import com.yu.comment.domain.vo.CommentVO;
import com.yu.comment.service.ICommentService;
import com.yu.common.domain.AjaxResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final ICommentService commentService;


    @ApiOperation("查询所有评论")
    @GetMapping("/all/{id}")
    public AjaxResult<List<CommentsVO>> listComments(@PathVariable Long id){
        log.info("查询所有评论");
        return AjaxResult.success(commentService.listComments(id));
    }

    @ApiOperation("发表评论")
    @PostMapping
    public AjaxResult<Void> comment(@RequestBody  CommentDTO commentDTO){
        commentService.comment(commentDTO);
        return AjaxResult.success();
    }

    @ApiOperation("删除评论")
    @ApiImplicitParam(name = "id", value = "评论ID")
    @DeleteMapping("/{id}")
    public AjaxResult<Void> deleteMyComment(@PathVariable Long id){
        commentService.deleteMyComment(id);
        return AjaxResult.success();
    }

    @ApiOperation("查询用户自己的评论")
    @ApiImplicitParam(name = "id", value = "订单详情ID")
    @GetMapping("/user")
    public AjaxResult<List<CommentsVO>> getMyComments(){
        log.info("查询订单详情{}的评论");
        return AjaxResult.success(commentService.getMyComments());
    }

    @PostMapping("/like/{id}")
    @ApiOperation("点赞评论")
    @ApiImplicitParam(name = "id", value = "评论ID")
    public AjaxResult<Void> likeComment(@PathVariable Long id){
        log.info("点赞评论{}", id);
        commentService.likeComment(id);
        return AjaxResult.success();
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("查询用户评论详情")
    public AjaxResult<CommentVO> getUserCommentDetail(@PathVariable Long id){
        log.info("查询用户评论详情");
        return AjaxResult.success(commentService.getUserCommentDetail(id));
    }

}
