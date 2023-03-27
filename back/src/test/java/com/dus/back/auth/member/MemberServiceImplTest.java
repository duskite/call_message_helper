package com.dus.back.auth.member;

import com.dus.back.member.Member;
import com.dus.back.member.MemberService;
import com.dus.back.member.MemberType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Test
    void save(){
        Member member = new Member();
        member.setUserId("aaaa");
        member.setPassword("1234");
        member.setMemberType(MemberType.PERSONAL);

        Long saveId = memberService.addMember(member);
        Member saveMember = memberService.findById(saveId);

        Assertions.assertThat(member).isEqualTo(saveMember);
    }

    @Test
    void findByUserId(){
        String userId = "asda";

        Member member = new Member();
        member.setUserId(userId);
        memberService.addMember(member);

        Member findMember = memberService.findByUserId(userId);
        Assertions.assertThat(findMember.getUserId()).isEqualTo(member.getUserId());
    }

}