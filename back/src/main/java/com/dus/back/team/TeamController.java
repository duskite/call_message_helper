package com.dus.back.team;

import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.vaild.CheckTeamNameValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class TeamController {

    private final TeamService teamService;
    private final CheckTeamNameValidator checkTeamNameValidator;

    public TeamController(TeamService teamService, CheckTeamNameValidator checkTeamNameValidator) {
        this.teamService = teamService;
        this.checkTeamNameValidator = checkTeamNameValidator;
    }

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkTeamNameValidator);
    }


    @GetMapping("/team-page/admin/{adminUserId}")
    public String teamPage(Model model, Authentication authentication) {

        String adminUserId = authentication.getName();
        List<Team> teamList = teamService.findAllByAdminUserId(adminUserId);

        model.addAttribute("adminUserId", adminUserId);
        model.addAttribute("teamList", teamList);

        return "/team/team-page";
    }

    @GetMapping("/team/create-form")
    public String createForm(Model model, TeamDTO teamDTO, Authentication authentication) {
        teamDTO.setAdminUserId(authentication.getName());
        model.addAttribute("teamDTO", teamDTO);
        return "/team/create-form";
    }

    @PostMapping("/team")
    public String teamAdd(@Valid TeamDTO teamDTO, Errors errors, Model model, Authentication authentication) {
        if (errors.hasErrors()) {
            log.info("팀 생성 에러 발생");
            model.addAttribute("teamDTO", teamDTO);

            Map<String, String> validatorResult = teamService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/team/create-form";
        }

        teamService.addTeam(teamDTO.toEntity());

        return "redirect:/team-page/admin/" + authentication.getName();
    }


    @DeleteMapping("/team")
    public String teamDelete() {
        return null;
    }

    @PostMapping("/team/invite")
    public String teamInvite(InviteDTO inviteDTO) {

        log.info("팀원 초대. 초대 되는 팀명: {}", inviteDTO.getTeamName());
        log.info("팀원 초대. 초대 되는 팀 관리자 ID: {}", inviteDTO.getAdminUserId());
        log.info("팀원 초대. 초대 받는 사람 ID: {}", inviteDTO.getInviteeUserId());

        teamService.createInvite(inviteDTO.toEntity());

        return "/team/team-member-list";
    }

    @GetMapping("/team/members")
    public String teamMembers(TeamDTO teamDTO, Model model) {
        Team findTeam = teamService.findByTeamName(teamDTO.getTeamName());
        List<Member> memberList = findTeam.getMembers();

        model.addAttribute("memberList", memberList);
        model.addAttribute("teamName", teamDTO.getTeamName());
        model.addAttribute("adminUserId", teamDTO.getAdminUserId());

        return "/team/team-member-list";

    }

}
