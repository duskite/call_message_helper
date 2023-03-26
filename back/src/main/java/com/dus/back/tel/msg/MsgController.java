package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/msg")
public class MsgController {
    private final MsgService msgService;

    public MsgController(MsgService msgService) {
        this.msgService = msgService;
    }

    @PostMapping("/sms")
    @ResponseBody
    public boolean sendSms(TelDTO telDTO) {
        log.error(telDTO.getTargetPhoneNumber());

        return msgService.sendSms(telDTO);
    }

    @PostMapping("/mms")
    @ResponseBody
    public boolean sendMms(TelDTO telDTO) {

        return msgService.sendMms(telDTO);
    }
}
