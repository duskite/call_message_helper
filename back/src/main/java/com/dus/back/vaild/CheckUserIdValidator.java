package com.dus.back.vaild;

import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberDTO;
import com.dus.back.member.MemberRepository;
import com.dus.back.member.MemberService;
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

    private final MemberService memberService;
    public CheckUserIdValidator(MemberService memberService) {
        this.memberService = memberService;
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

        try{
            memberService.duplicateCheck(memberDTO.toEntity());
        }catch (DuplicateException e){
            errors.rejectValue("userId", "아이디 중복", "이미 사용중인 아이디입니다.");
        }
    }
}
