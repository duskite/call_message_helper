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

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
class FcmServiceImplTest {

    @Autowired FcmService fcmService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("fcm 등록")
    void save() {
        Fcm fcm = createFcm();

        fcmService.addOrModifyFcm(fcm);

        Fcm findFcm = fcmService.findById(fcm.getId());
        Assertions.assertThat(findFcm).isEqualTo(fcm);
    }

    @Test
    @DisplayName("등록된 fcm 삭제")
    void remove() {
        Fcm fcm = createFcm();

        fcmService.addOrModifyFcm(fcm);
        fcmService.deleteFcm(fcm);

        Assertions.assertThatThrownBy(() -> {
            fcmService.findById(fcm.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("fcm 중복 등록 불가 체크")
    void duplicateFcm() {

        Fcm fcm1 = createFcm();
        Fcm fcm2 = createFcm();

        fcmService.addOrModifyFcm(fcm1);
        Assertions.assertThatThrownBy(() -> {
            fcmService.addOrModifyFcm(fcm2);
        }).isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("fcm 중복은 전화번호로만 체크함")
    void insertMembers(){
        IntStream.rangeClosed(0, 9).forEach(i ->{
            Fcm fcm = Fcm.builder()
                    .phoneNumber("0101234567" + i)
                    .token("aaa").build();

            fcmService.addOrModifyFcm(fcm);
        });
    }

    @Test
    @DisplayName("회원 가입 후 fcm 등록")
    void signupAndFcmRegister() {
        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .build();

        Fcm fcm = createFcm();
        fcm.setMember(member);

        fcmService.addOrModifyFcm(fcm);
    }

    @Test
    @DisplayName("한 고객이 여러 휴대폰 등록")
    void manyFcmRegister() {
        Member member = Member.builder()
                .userId("ysy")
                .password("1234")
                .build();

        IntStream.rangeClosed(1,9).forEach( i->{
            Fcm fcm = Fcm.builder()
                    .userId("ysy")
                    .phoneNumber("0101234567" + i)
                    .token("asd")
                    .build();

            fcm.setMember(member);
        });

    }

    Fcm createFcm(){
        Fcm fcm = Fcm.builder()
                .phoneNumber("01012345678")
                .token("a")
                .build();

        return fcm;
    }

}