package com.dus.back.auth;

import com.dus.back.auth.fcm.FcmDTO;
import com.dus.back.auth.fcm.FcmService;
import com.dus.back.auth.member.MemberDTO;
import com.dus.back.auth.member.MemberService;
import com.dus.back.auth.member.MemberType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final FcmService fcmService;
    private final MemberService memberService;

    public AuthController(FcmService fcmService, MemberService memberService) {
        this.fcmService = fcmService;
        this.memberService = memberService;
    }

    @PostMapping(value = "/member", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String saveMember(@Param("userId") String userId, @Param("password") String password){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserId(userId);
        memberDTO.setPassword(password);
        memberDTO.setMemberType(MemberType.PERSONAL);

        memberService.save(memberDTO.toEntity());

        return "redirect:/";
    }

    @GetMapping("/member/signup-form")
    public String signUpView(){
        return "/member/signup-form";
    }

    @GetMapping("/member/signin-form")
    public String signInView(){
        return "/member/signin-form";
    }


    @PostMapping("/fcm-token")
    @ResponseBody
    public Long saveFcm(@RequestBody FcmDTO fcmDTO){
        System.out.println(fcmDTO.getPhoneNumber());
        return fcmService.save(fcmDTO.toEntity());
    }
}
