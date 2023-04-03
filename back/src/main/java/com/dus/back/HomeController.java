package com.dus.back;

import com.dus.back.boilerplate.BoilerplateService;
import com.dus.back.domain.*;
import com.dus.back.fcm.FcmService;
import com.dus.back.firebase.RequestFcmDTO;
import com.dus.back.firebase.RequestFcmService;
import com.dus.back.firebase.RequestFcmType;
import com.dus.back.member.MemberService;
import com.dus.back.member.MemberType;
import com.dus.back.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 사용자별 메인 화면을 보여주는 컨트롤러
 */
@Slf4j
@Controller
public class HomeController {

    private final FcmService fcmService;
    private final BoilerplateService boilerplateService;
    private final RequestFcmService requestFcmService;
    private final MemberService memberService;
    private final TeamService teamService;

    public HomeController(FcmService fcmService, BoilerplateService boilerplateService, RequestFcmService requestFcmService, MemberService memberService, TeamService teamService) {
        this.fcmService = fcmService;
        this.boilerplateService = boilerplateService;
        this.requestFcmService = requestFcmService;
        this.memberService = memberService;
        this.teamService = teamService;
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication){


        String userId = authentication.getName();
        log.info("현재 로그인 된 유저: {}", userId);

        if(!authentication.isAuthenticated()){
            return "/member/sign-in";
        }


        Member findMember = memberService.findByUserId(userId);

        boolean isBusinessUser = memberService.isBusinessUser(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("isBusinessUser", isBusinessUser);

        List<Team> teamList = findMember.getTeams();
        model.addAttribute("teamList", teamList);


        List<String> myPhoneNumberList = fcmService.findAllPhoneNumbersByUserId(userId);
        model.addAttribute("myPhoneNumberList", myPhoneNumberList);
        standByPhone(userId, myPhoneNumberList);

        List<Boilerplate> boilerplateList = boilerplateService.findAllPersonalBoilerplate(userId);
        model.addAttribute("boilerplateList", boilerplateList);


        List<Invitation> findInvitationList = teamService.findAllInviteByInviteeUserId(userId);
        model.addAttribute("invitationList", findInvitationList);


        return "home";
    }

    /**
     * 로그인시 휴대폰 깨우기 요청
     * @param myPhoneNumbers
     */
    private void standByPhone(String userId, List<String> myPhoneNumbers) {
        for (String phoneNumber : myPhoneNumbers) {
            Fcm findFcm = fcmService.findByUserIdAndPhoneNumber(userId, phoneNumber);
            RequestFcmDTO requestFcmDTO = new RequestFcmDTO();
            requestFcmDTO.setRequestFcmType(RequestFcmType.STAND_BY);
            requestFcmService.sendFcmMessage(findFcm.getToken(), requestFcmDTO);
        }
    }

}
