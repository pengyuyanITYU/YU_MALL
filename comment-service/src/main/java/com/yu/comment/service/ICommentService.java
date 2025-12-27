package com.yu.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.comment.domain.dto.CommentDTO;
import com.yu.comment.domain.po.Comment;
import com.yu.comment.domain.vo.CommentsVO;
import com.yu.comment.domain.vo.CommentVO;
import java.util.List;

public interface ICommentService extends IService<Comment> {
    List<CommentsVO> listComments(Long id);

    void comment(CommentDTO commentDTO);

    void deleteMyComment(Long id);

    List<CommentsVO> getMyComments();

    void likeComment(Long id);

    CommentVO getUserCommentDetail(Long id);
}
