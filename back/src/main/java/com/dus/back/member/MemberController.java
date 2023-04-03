package com.dus.back.member;

import com.dus.back.domain.Member;
import com.dus.back.member.email.VerifyEmailService;
import com.dus.back.security.LoginDTO;
import com.dus.back.vaild.CheckUserIdValidator;
import com.sun.mail.util.MailConnectException;
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
import java.util.NoSuchElementException;

@Controller
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final CheckUserIdValidator checkUserIdValidator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerifyEmailService verifyEmailService;

    public MemberController(MemberService memberService, CheckUserIdValidator checkUserIdValidator, BCryptPasswordEncoder passwordEncoder, VerifyEmailService verifyEmailService) {
        this.memberService = memberService;
        this.checkUserIdValidator = checkUserIdValidator;
        this.passwordEncoder = passwordEncoder;
        this.verifyEmailService = verifyEmailService;
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

        authentication.setAuthenticated(false);

        return memberService.deleteMember(userId);
    }

    @PostMapping("/member/{userId}/password")
    public String passwordModify(@PathVariable("userId") String userId, @Valid MemberDTO memberDTO, Errors errors,
                                 Authentication authentication, Model model) {

        if (!memberDTO.getUserId().equals(authentication.getName())) {
            return "forbidden";
        }

        if (!errors.getFieldErrors("password").isEmpty()) {

            log.info("비밀번호 변경 에러 발생");
            model.addAttribute("userId", userId);
            model.addAttribute("memberDTO", memberDTO);

            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {

                if (!key.equals("password")) {
                    continue;
                }

                log.info("비밀번호 변경 에러 key: {}", key);
                log.info("비밀번호 변경 에러 내용: {}", validatorResult.get(key));
                model.addAttribute(key, validatorResult.get(key));
            }

            return "/member/info";
        }

        log.info("비밀번호 변경 요청");
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        if (memberService.modifyPassword(memberDTO.toEntity())) {

            log.info("비밀번호 변경 성공");
            return "redirect:/member/sign-in";
        } else {
            return "/member/info";
        }
    }

    @PostMapping("/member/{userId}/member-type")
    @ResponseBody
    public boolean memberTypeModify(@PathVariable("userId") String userId, MemberDTO memberDTO) {

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
    public String signIn(Model model, @Nullable @RequestParam("error") String error, @Nullable @RequestParam("exception") String exception) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginDTO", new LoginDTO());

        return "/member/sign-in";
    }

    @GetMapping("/member/{userId}/info")
    public String info(Model model, Authentication authentication, @PathVariable("userId") String userId,
                       MemberDTO memberDTO) {
        if (!userId.equals(authentication.getName())) {
            return "forbidden";
        }

        model.addAttribute("userId", userId);
        model.addAttribute("memberDTO", memberDTO);

        return "/member/info";
    }

    @GetMapping("/member/find-password")
    public String findPassword(Model model) {
        model.addAttribute("userId", new String());
        return "/member/find-password";
    }

    @PostMapping("/member/find-password/{userId}")
    @ResponseBody
    public boolean sendEmailForFindPassword(@PathVariable("userId") String userId) {

        log.info("비밀번호 찾기 요청");

        try {
            Member findMember = memberService.findByUserId(userId);
            String email = findMember.getEmail();

            String tempPassword = verifyEmailService.makeTempPassword();
            findMember.updatePassword(passwordEncoder.encode(tempPassword));
            memberService.modifyPassword(findMember);

            log.info("비밀번호 찾기 요청. userId: {}", userId);
            log.info("비밀번호 찾기 요청. email: {}", email);


            verifyEmailService.sendTempPassword(email, tempPassword);

            return true;
        }catch (NoSuchElementException e){
            return false;
        }catch (MailConnectException e) {
            return false;
        }
    }
}
