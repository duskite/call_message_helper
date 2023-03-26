package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/msg")
public class MsgController {
    private final MsgService msgService;

    public MsgController(MsgService msgService) {
        this.msgService = msgService;
    }

    @PostMapping("/sms")
    public String sendSms(TelDTO telDTO) {
        msgService.sendSms(telDTO);

        return "redirect:/home";
    }

    @PostMapping("/mms")
    public String sendMms(TelDTO telDTO) {
        msgService.sendMms(telDTO);

        return "redirect:/home";
    }
}
