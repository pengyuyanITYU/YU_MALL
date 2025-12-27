package com.yu.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yu.common.utils.BeanUtils;
import com.yu.member.domain.po.Member;
import com.yu.member.domain.vo.MemberVO;
import com.yu.member.mapper.MemberMapper;
import com.yu.member.service.IMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>  implements IMemberService {



    public MemberVO getMemberById(Integer id) {
         Member member = getById(id);
         log.info("查询会员{}",member);
        MemberVO memberVO = BeanUtils.copyBean(member, MemberVO.class);
        return memberVO;
    }

    @Override
    public List<MemberVO> listByMember() {
        List<Member> list = list();
        List<MemberVO> memberVOS = BeanUtils.copyToList(list, MemberVO.class);
        return memberVOS;
    }
}
