package com.yu.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.member.domain.po.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
