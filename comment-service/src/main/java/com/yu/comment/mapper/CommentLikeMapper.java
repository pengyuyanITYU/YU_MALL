package com.yu.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.comment.domain.po.CommentLikePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLikePO> {

}
