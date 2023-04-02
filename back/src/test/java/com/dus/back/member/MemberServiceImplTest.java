package com.dus.back.member;

import com.dus.back.boilerplate.BoilerplateService;
import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Member;
import com.dus.back.exception.DuplicateException;
import com.dus.back.fcm.FcmService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;


    @Test
    @DisplayName("회원 가입")
    void save(){
        Member member = createMember();

        memberService.addMember(member);
        Member findMember = memberService.findById(member.getId());

        Assertions.assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 가입 후 userId로 조회")
    void findByUserId() {
        Member member = createMember();

        memberService.addMember(member);
        Member findMember = memberService.findByUserId(member.getUserId());

        Assertions.assertThat(member.getUserId()).isEqualTo(findMember.getUserId());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void remove() {
        Member member = createMember();

        memberService.addMember(member);
        memberService.deleteMember(member.getUserId());

        Assertions.assertThatThrownBy(() -> {
            memberService.findById(member.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원가입 체크: 동일한 아이디로 회원가입 할 수 없음")
    void duplicateUserId() {
        Member member1 = createMember();
        Member member2 = createMember();

        memberService.addMember(member1);
        Assertions.assertThatThrownBy(() -> {
            memberService.addMember(member2);
        }).isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void modifyPassword() {
        Member member = createMember();

        memberService.addMember(member);
        Member findMember = memberService.findById(member.getId());
        findMember.updatePassword("1111");
        Member passwordChangedMember = memberService.findById(findMember.getId());

        Assertions.assertThat(passwordChangedMember.getPassword()).isEqualTo("1111");
    }

    @Test
    @DisplayName("회원 가입 여러건 체크: 아이디가 다를 경우 성공함")
    void insertMembers(){
        IntStream.rangeClosed(1, 100).forEach(i ->{
            Member member = Member.builder()
                    .email("y@y.y")
                    .password("1234")
                    .memberType(MemberType.PERSONAL)
                    .userId("testUser" + i)
                    .build();

            memberService.addMember(member);
        });
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