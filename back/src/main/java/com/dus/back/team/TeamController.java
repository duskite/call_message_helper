package com.dus.back.team;

import com.dus.back.domain.Invitation;
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
        try{
            List<Team> teamList = teamService.findAllByAdminUserId(adminUserId);
            model.addAttribute("teamList", teamList);
        }catch (NoSuchElementException e){
            log.info("아직 생성한 팀이 없음");
        }

        model.addAttribute("adminUserId", adminUserId);


        return "/team/team-manage";
    }

    @GetMapping("/team-create")
    public String teamCreatePage(Model model, TeamDTO teamDTO, Authentication authentication) {
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

        return "redirect:/team-manage/admin/" + authentication.getName();
    }


    @DeleteMapping("/team")
    public String teamDelete(TeamDTO teamDTO, Model model, Authentication authentication) {


        teamService.deleteTeam(teamDTO.toEntity());

        List<Team> teamList = teamService.findAllByAdminUserId(teamDTO.getAdminUserId());
        model.addAttribute("teamList", teamList);

        return "/team/team-manage :: #team-list";
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
    @DeleteMapping("/team/{teamName}/members/{userId}")
    public String teamMemberDelete(@PathVariable("teamName") String teamName, @PathVariable("userId") String userId,
                                   Model model, Authentication authentication) {

        log.info("팀원 삭제 요청. 팀이름: {}", teamName);
        log.info("팀원 삭제 요청. 유저ID: {}", userId);

        //팀원 삭제 처리
        teamService.deleteTeamMember(teamName, userId);

        //새로 로드해서 내려주기
        Team findTeam = teamService.findByTeamName(teamName);
        List<Member> memberList = findTeam.getMembers();

        model.addAttribute("memberList", memberList);
        model.addAttribute("teamName", teamName);
        model.addAttribute("adminUserId", authentication.getName());

        return "/team/team-manage :: #team-member-list";
    }




    @PostMapping("/team/invitation")
    @ResponseBody
    public boolean createInvitation(InvitationDTO invitationDTO) {

        log.info("초대장. 초대 되는 팀명: {}", invitationDTO.getTeamName());
        log.info("초대장 초대 되는 팀 관리자 ID: {}", invitationDTO.getAdminUserId());
        log.info("초대장 초대 받는 사람 ID: {}", invitationDTO.getInviteeUserId());

        return teamService.createInvite(invitationDTO.toEntity());
    }

    @PostMapping("/team/invitation/{uniqueKey}")
    public String acceptInvitation(@PathVariable("uniqueKey") String uniqueKey, Authentication authentication, Model model) {

        InvitationDTO invitationDTO = uniqueKeyConvertInvitationDTO(uniqueKey);
        teamService.acceptInvite(invitationDTO.toEntity());


        List<Invitation> findInvitationList = teamService.findAllInviteByInviteeUserId(authentication.getName());
        model.addAttribute("invitationList", findInvitationList);


        return "/home :: #invitation-list";
    }

    @DeleteMapping("/team/invitation/{uniqueKey}")
    public String rejectInvitation(@PathVariable("uniqueKey") String uniqueKey, Authentication authentication, Model model) {

        InvitationDTO invitationDTO = uniqueKeyConvertInvitationDTO(uniqueKey);
        teamService.rejectInvite(invitationDTO.toEntity());

        List<Invitation> findInvitationList = teamService.findAllInviteByInviteeUserId(authentication.getName());
        model.addAttribute("invitationList", findInvitationList);

        return "/home :: #invitation-list";
    }

    private InvitationDTO uniqueKeyConvertInvitationDTO(String uniqueKey){
        String[] s = uniqueKey.split("_");
        InvitationDTO invitationDTO = new InvitationDTO();
        invitationDTO.setTeamName(s[0]);
        invitationDTO.setInviteeUserId(s[1]);

        return invitationDTO;
    }
}
