package com.dus.back.auth;

import com.dus.back.auth.fcm.FcmService;
import com.dus.back.auth.member.Member;
import com.dus.back.auth.member.MemberDTO;
import com.dus.back.auth.member.MemberService;
import com.dus.back.auth.security.LoginDTO;
import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 화면을 보여주는 컨트롤러
 */
@Slf4j
@Controller
public class HomeController {

    private final MemberService memberService;
    private final FcmService fcmService;

    public HomeController(MemberService memberService, FcmService fcmService) {
        this.memberService = memberService;
        this.fcmService = fcmService;
    }

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("telDTO", new TelDTO());
        return "home";
    }

    @GetMapping("/member/signup-form")
    public String signUp(Model model, MemberDTO memberDTO) {
        model.addAttribute("memberDTO", memberDTO);
        return "/member/signup-form";
    }

    @GetMapping("/member/signin-form")
    public String signIn(Model model, @Nullable @RequestParam("error") String error, @Nullable @RequestParam("exception") String exception){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginDTO", new LoginDTO());

        return "/member/signin-form";
    }

    @GetMapping("/member/info/{userId}")
    public String infoView(Model model, @RequestParam String userId) {
        Member member = memberService.findByUserId(userId);
        List<String> phoneNumbers = fcmService.findAllPhoneNumbers(member.getUserId());
        model.addAttribute("phoneNumbers", phoneNumbers);

        return "/member/info";
    }
}
