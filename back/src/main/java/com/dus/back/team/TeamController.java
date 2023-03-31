package com.dus.back.team;

import com.dus.back.domain.Invite;
import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.vaild.CheckTeamNameValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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


    @GetMapping("/team-manage/admin/{adminUserId}")
    public String teamManage(Model model, Authentication authentication) {

        String adminUserId = authentication.getName();
        List<Team> teamList = teamService.findAllByAdminUserId(adminUserId);

        model.addAttribute("adminUserId", adminUserId);
        model.addAttribute("teamList", teamList);

        return "/team/team-manage";
    }

    @GetMapping("/team-create")
    public String teamCreate(Model model, TeamDTO teamDTO, Authentication authentication) {
        teamDTO.setAdminUserId(authentication.getName());
        model.addAttribute("teamDTO", teamDTO);
        return "/team/team-create";
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

            return "/team/team-create";
        }

        teamService.addTeam(teamDTO.toEntity());

        return "redirect:/team-page/admin/" + authentication.getName();
    }


    @DeleteMapping("/team")
    public String teamDelete() {
        return null;
    }

    @GetMapping("/team/members")
    public String teamMembers(TeamDTO teamDTO, Model model) {
        Team findTeam = teamService.findByTeamName(teamDTO.getTeamName());
        List<Member> memberList = findTeam.getMembers();

        model.addAttribute("memberList", memberList);
        model.addAttribute("teamName", teamDTO.getTeamName());
        model.addAttribute("adminUserId", teamDTO.getAdminUserId());

        return "/team/team-manage :: #team-member-list";

    }


    @PostMapping("/team/invitation")
    @ResponseBody
    public void createInvitation(InviteDTO inviteDTO) {

        log.info("초대장. 초대 되는 팀명: {}", inviteDTO.getTeamName());
        log.info("초대장 초대 되는 팀 관리자 ID: {}", inviteDTO.getAdminUserId());
        log.info("초대장 초대 받는 사람 ID: {}", inviteDTO.getInviteeUserId());

        teamService.createInvite(inviteDTO.toEntity());
    }

    @PostMapping("/team/invitation/{uniqueKey}")
    public String acceptInvitation(@PathVariable("uniqueKey") String uniqueKey, Authentication authentication, Model model) {

        InviteDTO inviteDTO = uniqueKeyConvertInviteDTO(uniqueKey);
        teamService.acceptInvite(inviteDTO.toEntity());

        try {
            List<Invite> findInviteList = teamService.findAllInviteByInviteeUserId(authentication.getName());
            model.addAttribute("inviteList", findInviteList);
        } catch (NoSuchElementException e) {
            log.info("받은 초대가 없음");
        }

        return "/home :: #invitation-list";
    }

    @DeleteMapping("/team/invitation/{uniqueKey}")
    public String rejectInvitation(@PathVariable("uniqueKey") String uniqueKey, Authentication authentication, Model model) {

        InviteDTO inviteDTO = uniqueKeyConvertInviteDTO(uniqueKey);
        teamService.rejectInvite(inviteDTO.toEntity());

        try {
            List<Invite> findInviteList = teamService.findAllInviteByInviteeUserId(authentication.getName());
            model.addAttribute("inviteList", findInviteList);
        } catch (NoSuchElementException e) {
            log.info("받은 초대가 없음");
        }

        return "/home :: #invitation-list";
    }

    private InviteDTO uniqueKeyConvertInviteDTO(String uniqueKey){
        String[] s = uniqueKey.split("_");
        InviteDTO inviteDTO = new InviteDTO();
        inviteDTO.setTeamName(s[0]);
        inviteDTO.setInviteeUserId(s[1]);

        return inviteDTO;
    }
}
