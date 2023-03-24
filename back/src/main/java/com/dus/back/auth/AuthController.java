package com.dus.back.auth;

import com.dus.back.auth.fcm.Fcm;
import com.dus.back.auth.fcm.FcmDTO;
import com.dus.back.auth.fcm.FcmService;
import com.dus.back.auth.member.MemberDTO;
import com.dus.back.auth.member.MemberService;
import com.dus.back.auth.vaild.CheckUserIdValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/auth/*")
public class AuthController {
    private final FcmService fcmService;
    private final MemberService memberService;
    private final CheckUserIdValidator checkUserIdValidator;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(FcmService fcmService, MemberService memberService, CheckUserIdValidator checkUserIdValidator, BCryptPasswordEncoder passwordEncoder) {
        this.fcmService = fcmService;
        this.memberService = memberService;
        this.checkUserIdValidator = checkUserIdValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkUserIdValidator);
    }

    @PostMapping("/member")
    public String saveMember(@Valid MemberDTO memberDTO, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("memberDTO", memberDTO);

            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/member/signup-form";
        }

        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberService.save(memberDTO.toEntity());
        return "redirect:/member/signin-form";
    }

    @PostMapping("/fcm-token")
    @ResponseBody
    public Long saveFcm(@RequestBody FcmDTO fcmDTO){

        return fcmService.save(fcmDTO.toEntity());
    }

}
