package com.dus.back;

import com.dus.back.fcm.FcmService;
import com.dus.back.member.MemberService;
import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 사용자별 메인 화면을 보여주는 컨트롤러
 */
@Slf4j
@Controller
public class HomeController {

    private final FcmService fcmService;

    public HomeController(FcmService fcmService) {
        this.fcmService = fcmService;
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication){

        String userId = authentication.getName();
        List<String> myPhoneNumbers = fcmService.findAllPhoneNumbers(userId);
        boolean hasNumbers = (myPhoneNumbers.size() > 0) ? true: false;

        model.addAttribute("userId", userId);
        model.addAttribute("hasNumbers", hasNumbers);
        model.addAttribute("myPhoneNumbers", myPhoneNumbers);

        return "home";
    }

}
