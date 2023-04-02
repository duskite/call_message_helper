package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Member;
import com.dus.back.member.MemberService;
import com.dus.back.member.MemberType;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;

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
    @DisplayName("상용구 1건 등록")
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

    @Test
    @DisplayName("상용구 여러건 등록: 상용구 제목이 다른 경우는 성공함")
    void duplicateBoilerplates() {
        Member member = createMember();
        memberService.addMember(member);

        IntStream.rangeClosed(1,10).forEach(i ->{
            Boilerplate boilerplate = Boilerplate.builder()
                    .subject("test" + i)
                    .msg("테스트")
                    .authorUserId("ysy")
                    .build();

            em.persist(boilerplate);
            boilerplate.setMember(member);
        });
    }

    @Test
    @DisplayName("상용구 여러건 등록: 상용구 제목이 같은 경우는 실패함")
    void duplicateBoilerplatesSameSubject() {
        Member member = createMember();
        memberService.addMember(member);

        Assertions.assertThatThrownBy(() -> {
            IntStream.rangeClosed(1, 2).forEach(i ->{
                Boilerplate boilerplate = Boilerplate.builder()
                        .subject("test")
                        .msg(String.valueOf(i))
                        .authorUserId("ysy")
                        .build();

                em.persist(boilerplate);
                boilerplate.setMember(member);
            });
        }).isInstanceOf(PersistenceException.class);
    }


    Member createMember() {

        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .memberType(MemberType.PERSONAL)
                .email("ysy@naver.com")
                .build();

        return member;
    }
}