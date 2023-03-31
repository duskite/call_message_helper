package com.dus.back.vaild;

import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberDTO;
import com.dus.back.team.TeamDTO;
import com.dus.back.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * 팀 생성시 teamName 중복 체크 담당
 */
@Slf4j
@Component
public class CheckTeamNameValidator implements Validator {

    private final TeamService teamService;

    public CheckTeamNameValidator(TeamService teamService) {
        this.teamService = teamService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            doValidate((TeamDTO) target, errors);
        } catch (RuntimeException e) {
            log.error("중복 검증 에러");
            throw e;
        }

    }

    public void doValidate(TeamDTO teamDTO, Errors errors) {

        try{
            teamService.duplicateCheck(teamDTO.toEntity());
        }catch (DuplicateException e){
            errors.rejectValue("teamName", "팀 이름 중복", "이미 사용중인 팀 이름입니다.");
        }
    }
}
