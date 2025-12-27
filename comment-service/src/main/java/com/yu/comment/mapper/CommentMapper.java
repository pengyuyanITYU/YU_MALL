package com.yu.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.comment.domain.po.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
