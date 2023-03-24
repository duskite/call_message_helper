package com.dus.back.auth.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired MemberService memberService;

    @Test
    void save(){
        Member member = new Member();
        member.setUserId("aaaa");
        member.setPassword("1234");

        Long saveId = memberService.save(member);
        Member saveMember = memberService.findById(saveId);

        Assertions.assertThat(member).isEqualTo(saveMember);
    }

}