package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Member;
import com.dus.back.member.MemberService;
import com.dus.back.member.MemberType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoilerplateServiceImplTest {

    @Autowired BoilerplateService boilerplateService;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("상용구 등록")
    void addBoilerplate(){
        Member member = createMember();
        memberService.addMember(member);

        Boilerplate boilerplate = Boilerplate.builder()
                .subject("test")
                .msg("테스트")
                .authorUserId("ysy")
                .build();
        em.persist(boilerplate);
        boilerplate.setMember(member);

        Boilerplate findBoilerplate = boilerplateService.findById(boilerplate.getId());
        Assertions.assertThat(boilerplate).isEqualTo(findBoilerplate);
    }

    Member createMember(){
        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .memberType(MemberType.PERSONAL)
                .email("ysy@naver.com")
                .build();

        return member;
    }
}