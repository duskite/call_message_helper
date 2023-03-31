package com.dus.back.vaild;

import com.dus.back.boilerplate.BoilerplateDTO;
import com.dus.back.boilerplate.BoilerplateService;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class CheckBoilerplateValidator implements Validator {

    private final BoilerplateService boilerplateService;

    public CheckBoilerplateValidator(BoilerplateService boilerplateService) {
        this.boilerplateService = boilerplateService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            doValidate((BoilerplateDTO) target, errors);
        } catch (RuntimeException e) {
            log.error("중복 검증 에러");
            throw e;
        }

    }

    public void doValidate(BoilerplateDTO boilerplateDTO, Errors errors) {

        try{
            boilerplateService.duplicateCheck(boilerplateDTO.toEntity());
        }catch (DuplicateException e){
            log.info("중복 예외 발생");
            errors.rejectValue("subject", "상용구 중복", "상용구 제목은 중복 될 수 없습니다.");
        }
    }
}
