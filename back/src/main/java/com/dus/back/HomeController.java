package com.dus.back;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 화면을 보여주는 컨트롤러
 */
@Slf4j
@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("telDTO", new TelDTO());
        return "home";
    }
}
