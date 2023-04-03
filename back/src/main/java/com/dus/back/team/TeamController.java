package com.dus.back.team;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Invitation;
import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.member.MemberService;
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
    private final MemberService memberService;
    private final CheckTeamNameValidator checkTeamNameValidator;

    public TeamController(TeamService teamService, MemberService memberService, CheckTeamNameValidator checkTeamNameValidator) {
        this.teamService = teamService;
        this.memberService = memberService;
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
    @ResponseBody
    public boolean teamDelete(TeamDTO teamDTO, Authentication authentication) {

        return teamService.deleteTeam(teamDTO.toEntity());
    }

    @GetMapping("/team/{teamName}/members")
    public String teamMembers(@PathVariable("teamName")String teamName, Model model) {
        Team findTeam = teamService.findByTeamName(teamName);
        List<Member> memberList = findTeam.getMembers();


        model.addAttribute("memberList", memberList);
        model.addAttribute("teamName", findTeam.getTeamName());
        model.addAttribute("adminUserId", findTeam.getAdminUserId());

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


    @GetMapping("/team/{teamName}/boilerplates")
    public String teamBoilerplates(@PathVariable("teamName") String teamName, Model model) {
        Team findTeam = teamService.findByTeamName(teamName);
        List<Boilerplate> teamBoilerplateList = findTeam.getBoilerplates();
        model.addAttribute("teamBoilerplateList", teamBoilerplateList);

        log.info("팀 상용구들 가져오기. 팀 명: {}", teamName);
        log.info("팀 상용구들 가져오기. 상용구 개수: {}", teamBoilerplateList.size());

        return "/fragments/boilerplate-list :: team-boilerplate-list";
    }



    @PostMapping("/team/invitation")
    @ResponseBody
    public boolean createInvitation(InvitationDTO invitationDTO) {

        log.info("초대장. 초대 되는 팀명: {}", invitationDTO.getTeamName());
        log.info("초대장 초대 되는 팀 관리자 ID: {}", invitationDTO.getAdminUserId());
        log.info("초대장 초대 받는 사람 ID: {}", invitationDTO.getInviteeUserId());

        Team findTeam = teamService.findByTeamName(invitationDTO.getTeamName());
        try {
            Member findMember = memberService.findByUserId(invitationDTO.getInviteeUserId());
            if (findTeam.getMembers().contains(findMember)) {
                return false;
            }
        } catch (NoSuchElementException e) {
            return false;
        }

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
