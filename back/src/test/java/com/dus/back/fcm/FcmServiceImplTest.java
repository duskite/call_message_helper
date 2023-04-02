package com.dus.back.fcm;

import com.dus.back.domain.Fcm;
import com.dus.back.domain.Member;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
class FcmServiceImplTest {

    @Autowired FcmService fcmService;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원만 fcm 등록을 할 수 있음")
    void save() {
        Fcm fcm = Fcm.builder()
                .phoneNumber("01012345678")
                .token("asdasd")
                .userId("ysy").build();

        Assertions.assertThatThrownBy(() -> {
            fcmService.addOrModifyFcm(fcm);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원 가입 후 fcm 등록")
    void signupAndFcmRegister() {
        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .build();
        memberService.addMember(member);

        Fcm fcm = Fcm.builder()
                .phoneNumber("01012345678")
                .token("asdasd")
                .userId("ysy")
                .build();
        fcm.setMember(member);
        fcmService.addOrModifyFcm(fcm);
    }


    @Test
    @DisplayName("fcm 토큰 업데이트")
    void updateToken() {
        String originalToken = "aaa";

        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .build();
        memberService.addMember(member);
        Fcm fcm = Fcm.builder()
                .phoneNumber("01012345678")
                .token(originalToken)
                .userId("ysy")
                .build();
        fcmService.addOrModifyFcm(fcm);
        Fcm findFcm = fcmService.findById(fcm.getId());
        findFcm.updateToken("bbb");

        Fcm updatedTokenFcm = fcmService.findById(findFcm.getId());
        Assertions.assertThat(originalToken).isNotEqualTo(updatedTokenFcm.getToken());
    }

    @Test
    @DisplayName("동일한 전화번호로 fcm 등록 요청이 올 경우 token 정보 업데이트함")
    void duplicateFcm() {
        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .build();
        memberService.addMember(member);

        Fcm fcm1 = Fcm.builder()
                .phoneNumber("01012345678")
                .token("aaa")
                .userId("ysy")
                .build();
        Fcm fcm2 = Fcm.builder()
                .phoneNumber("01012345678")
                .token("bbb")
                .userId("ysy")
                .build();

        fcmService.addOrModifyFcm(fcm1);
        fcmService.addOrModifyFcm(fcm2);

        Fcm findFcm = fcmService.findByPhoneNumber("01012345678");
        Assertions.assertThat(findFcm.getToken()).isNotEqualTo("aaa");
        Assertions.assertThat(findFcm.getToken()).isEqualTo("bbb");
    }

    @Test
    @DisplayName("fcm 여러건 등록: 전화번호가 다른 경우는 성공함")
    void createManyFcmByOneUser(){
        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .build();
        memberService.addMember(member);

        IntStream.rangeClosed(0, 9).forEach(i ->{
            Fcm fcm = Fcm.builder()
                    .phoneNumber("0101234567" + i)
                    .token("aaa")
                    .userId("ysy")
                    .build();

            fcmService.addOrModifyFcm(fcm);
        });
    }

}