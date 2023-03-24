package com.dus.back.auth.vaild;

import com.dus.back.auth.member.MemberDTO;
import com.dus.back.auth.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 회원 가입시 userId 중복 체크를 담당하는 컴포넌트
 */
@Slf4j
@Component
public class CheckUserIdValidator implements Validator {

    private final MemberRepository memberRepository;

    public CheckUserIdValidator(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            doValidate((MemberDTO) target, errors);
        } catch (RuntimeException e) {
            log.error("중복 검증 에러");
            throw e;
        }
    }

    public void doValidate(MemberDTO memberDTO, Errors errors) {
        if(memberRepository.existByUserId(memberDTO.toEntity().getUserId())){
            errors.rejectValue("userId", "아이디 중복", "이미 사용중인 아이디입니다.");
        }
    }
}
