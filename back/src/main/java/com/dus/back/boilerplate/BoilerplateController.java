package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Team;
import com.dus.back.team.TeamService;
import com.dus.back.vaild.CheckBoilerplateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class BoilerplateController {

    private final BoilerplateService boilerplateService;
    private final CheckBoilerplateValidator checkBoilerplateValidator;
    private final TeamService teamService;

    public BoilerplateController(BoilerplateService boilerplateService, CheckBoilerplateValidator checkBoilerplateValidator, TeamService teamService) {
        this.boilerplateService = boilerplateService;
        this.checkBoilerplateValidator = checkBoilerplateValidator;
        this.teamService = teamService;
    }

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkBoilerplateValidator);
    }

    /**
     * 비동기 요청 후 list 부분을 내려주는데, error 발생시 현재 방법(뷰 리졸버 - 타임리프 템플릿 엔진 사용)으로는 클라이언트에 적절한 응답을 내려주기 힘듦
     * 별도로 헤더에 에러를 담아서 응답하는 것으로 처리(true:String)
     * @param httpServletResponse
     */
    private void customErrorResponse(HttpServletResponse httpServletResponse){
        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpServletResponse.setHeader("duplicate-error", "true");
    }


    @GetMapping("/boilerplate-manage/{userId}")
    public String boilerplatePage(Model model, Authentication authentication, @PathVariable("userId") String userId) {

        if(!userId.equals(authentication.getName())){
            return "forbidden";
        }

        List<Boilerplate> boilerplateList = boilerplateService.findAllPersonalBoilerplate(userId);

        model.addAttribute("boilerplateList", boilerplateList);
        model.addAttribute("userId", userId);
        model.addAttribute("isEditablePage", true);

        return "boilerplate/boilerplate-manage";
    }

    @GetMapping("/boilerplate-manage/{teamName}/{adminUserId}")
    public String boilerplateTeamPage(Model model, @PathVariable("teamName") String teamName,
                                      @PathVariable("adminUserId") String adminUserId, Authentication authentication) {

        if(!adminUserId.equals(authentication.getName())){
            return "forbidden";
        }

        model.addAttribute("teamName", teamName);
        model.addAttribute("adminUserId", adminUserId);

        Team findTeam = teamService.findByTeamName(teamName);
        List<Boilerplate> teamBoilerplateList = findTeam.getBoilerplates();
        model.addAttribute("teamBoilerplateList", teamBoilerplateList);
        model.addAttribute("isEditablePage", true);

        return "boilerplate/boilerplate-team-manage";
    }



    @PostMapping("/boilerplate")
    public String boilerplateAdd(@Valid BoilerplateDTO boilerplateDTO, Errors errors, Model model,
                                 Authentication authentication, HttpServletResponse httpServletResponse) {

        if (!boilerplateDTO.getAuthorUserId().equals(authentication.getName())) {
            return "forbidden";
        }


        log.info("boilerplate 등록 요청");

        if (errors.hasErrors()) {
            log.error("상용구 중복 등록 에러 발생");
            customErrorResponse(httpServletResponse);

            if (boilerplateDTO.getBoilerplateType() == BoilerplateType.TEAM) {
                return "fragments/boilerplate-list :: team-boilerplate-list";
            }else {
                return "fragments/boilerplate-list :: personal-boilerplate-list";
            }
        }


        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);

        if (boilerplateDTO.getBoilerplateType() == BoilerplateType.TEAM) {
            Team findTeam = teamService.findByTeamName(boilerplateDTO.getTeamName());
            boilerplateService.addTeamBoilerplate(boilerplateDTO.toEntity(), findTeam);

            List<Boilerplate> teamBoilerplateList = findTeam.getBoilerplates();
            model.addAttribute("teamBoilerplateList", teamBoilerplateList);

            return "fragments/boilerplate-list :: team-boilerplate-list";

        }else {
            boilerplateService.addBoilerplate(boilerplateDTO.toEntity());
            List<Boilerplate> boilerplateList = boilerplateService.findAllPersonalBoilerplate(authentication.getName());

            model.addAttribute("boilerplateList", boilerplateList);

            return "fragments/boilerplate-list :: personal-boilerplate-list";
        }

    }

    @PutMapping("/boilerplate")
    public String boilerplateUpdate(Model model, BoilerplateDTO boilerplateDTO, Authentication authentication) {

        if (!boilerplateDTO.getAuthorUserId().equals(authentication.getName())) {
            return "forbidden";
        }

        log.info("boilerplate 수정 요청");
        log.info("수정 요청 온 DTO: {}", boilerplateDTO.toString());

        boilerplateService.modifyBoilerplate(boilerplateDTO.toEntity());

        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);

        if (boilerplateDTO.getBoilerplateType() == BoilerplateType.TEAM) {
            Team findTeam = teamService.findByTeamName(boilerplateDTO.getTeamName());
            List<Boilerplate> teamBoilerplateList = findTeam.getBoilerplates();
            model.addAttribute("teamBoilerplateList", teamBoilerplateList);

            return "fragments/boilerplate-list :: team-boilerplate-list";

        }else {
            List<Boilerplate> boilerplateList = boilerplateService.findAllPersonalBoilerplate(authentication.getName());
            model.addAttribute("boilerplateList", boilerplateList);

            return "fragments/boilerplate-list :: personal-boilerplate-list";
        }


    }

    @DeleteMapping("/boilerplate")
    public String boilerplateDelete(Model model, BoilerplateDTO boilerplateDTO, Authentication authentication) {
        log.info("boilerplate 삭제 요청");

        if (!boilerplateDTO.getAuthorUserId().equals(authentication.getName())) {
            return "forbidden";
        }


        boilerplateService.deleteBoilerplate(boilerplateDTO.toEntity());

        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);

        if (boilerplateDTO.getBoilerplateType() == BoilerplateType.TEAM) {
            Team findTeam = teamService.findByTeamName(boilerplateDTO.getTeamName());
            List<Boilerplate> teamBoilerplateList = findTeam.getBoilerplates();
            model.addAttribute("teamBoilerplateList", teamBoilerplateList);

            return "fragments/boilerplate-list :: team-boilerplate-list";

        }else {
            List<Boilerplate> boilerplateList = boilerplateService.findAllPersonalBoilerplate(authentication.getName());
            model.addAttribute("boilerplateList", boilerplateList);

            return "fragments/boilerplate-list :: personal-boilerplate-list";
        }
    }
}
