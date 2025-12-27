package com.yu.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yu.member.domain.po.Member;
import com.yu.member.domain.vo.MemberVO;
import java.util.List;

public interface IMemberService extends IService<Member> {
    MemberVO getMemberById(Integer id);

    List<MemberVO> listByMember();
}
