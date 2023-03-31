package com.dus.back;

import com.dus.back.boilerplate.BoilerplateService;
import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Fcm;
import com.dus.back.domain.Invitation;
import com.dus.back.fcm.FcmService;
import com.dus.back.firebase.RequestFcmDTO;
import com.dus.back.firebase.RequestFcmService;
import com.dus.back.firebase.RequestFcmType;
import com.dus.back.member.MemberService;
import com.dus.back.team.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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

        List<String> myPhoneNumberList = fcmService.findAllPhoneNumbersByUserId(userId);
        standByPhone(myPhoneNumberList);

        List<Boilerplate> boilerplateList = boilerplateService.findAllBoilerplate(userId);
        model.addAttribute("boilerplateList", boilerplateList);

        boolean isBusinessUser = memberService.isBusinessUser(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("myPhoneNumberList", myPhoneNumberList);
        model.addAttribute("isBusinessUser", isBusinessUser);

        try {
            List<Invitation> findInvitationList = teamService.findAllInviteByInviteeUserId(userId);
            model.addAttribute("invitationList", findInvitationList);
        } catch (NoSuchElementException e) {
            log.info("아직 받은 초대가 없음");
        }

        return "home";
    }

    /**
     * 로그인시 휴대폰 깨우기 요청
     * @param myPhoneNumbers
     */
    private void standByPhone(List<String> myPhoneNumbers) {
        for (String phoneNumber : myPhoneNumbers) {
            Fcm findFcm = fcmService.findByPhoneNumber(phoneNumber);
            RequestFcmDTO requestFcmDTO = new RequestFcmDTO();
            requestFcmDTO.setRequestFcmType(RequestFcmType.STAND_BY);
            requestFcmService.sendFcmMessage(findFcm.getToken(), requestFcmDTO);
        }
    }

}
