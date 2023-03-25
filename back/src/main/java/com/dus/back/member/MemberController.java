package com.dus.back.member;

import com.dus.back.security.LoginDTO;
import com.dus.back.vaild.CheckUserIdValidator;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class MemberController {
    private final MemberService memberService;
    private final CheckUserIdValidator checkUserIdValidator;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberController(MemberService memberService, CheckUserIdValidator checkUserIdValidator, BCryptPasswordEncoder passwordEncoder) {
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


}
