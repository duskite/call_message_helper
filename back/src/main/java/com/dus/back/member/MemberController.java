package com.dus.back.member;

import com.dus.back.security.LoginDTO;
import com.dus.back.vaild.CheckUserIdValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@Slf4j
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
    public String memberAdd(@Valid MemberDTO memberDTO, Errors errors, Model model) {
        if (errors.hasErrors()) {
            log.info("회원 가입 에러 발생");
            model.addAttribute("memberDTO", memberDTO);

            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/member/sign-up";
        }

        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberDTO.setMemberType(MemberType.PERSONAL);

        memberService.addMember(memberDTO.toEntity());

        return "redirect:/member/sign-in";
    }

    @DeleteMapping("/member")
    @ResponseBody
    public boolean memberDelete(@RequestParam("userId") String userId, Authentication authentication) {
        if (!userId.equals(authentication.getName())) {
            return false;
        }

        memberService.deleteMember(userId);
        return true;
    }

    @PostMapping("/member/update/password")
    @ResponseBody
    public boolean passwordModify(MemberDTO memberDTO, Authentication authentication) {

        if(!memberDTO.getUserId().equals(authentication.getName())){
            return false;
        }

        log.info("비밀번호 변경 요청");
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        return memberService.modifyPassword(memberDTO.toEntity());
    }

    @PostMapping("/member/update/member-type")
    @ResponseBody
    public boolean memberTypeModify(MemberDTO memberDTO) {

        log.info("멤버 타입 변경 요청");

        memberDTO.setMemberType(MemberType.BUSINESS);
        return memberService.modifyMemberType(memberDTO.toEntity());
    }


    @GetMapping("/member/sign-up")
    public String signUp(Model model, MemberDTO memberDTO) {
        model.addAttribute("memberDTO", memberDTO);
        return "/member/sign-up";
    }

    @GetMapping("/member/sign-in")
    public String signIn(Model model, @Nullable @RequestParam("error") String error, @Nullable @RequestParam("exception") String exception){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginDTO", new LoginDTO());

        return "/member/sign-in";
    }

    @GetMapping("/member/info/{userId}")
    public String info(Model model, Authentication authentication, @PathVariable("userId") String userId) {

        if(!userId.equals(authentication.getName())){
            return "forbidden";
        }

        model.addAttribute("userId", userId);
        return "/member/info";

    }

}
