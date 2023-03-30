package com.dus.back.tel.call;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/call")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/stop")
    @ResponseBody
    public boolean stopCall(TelDTO telDTO, Authentication authentication){
        log.info("전화 요청. userId: {}", telDTO.getUserId());
        log.info("전화 요청. myPhoneNumber: {}", telDTO.getMyPhoneNumber());

        if(!telDTO.getUserId().equals(authentication.getName())){
            return false;
        }

        return callService.stopCall(telDTO);
    }

    @PostMapping("/start")
    @ResponseBody
    public boolean startCall(TelDTO telDTO, Authentication authentication){
        log.info("전화 요청. userId: {}", telDTO.getUserId());
        log.info("전화 요청. phoneNumber: {}", telDTO.getMyPhoneNumber());

        if(!telDTO.getUserId().equals(authentication.getName())){
            return false;
        }

        return callService.startCall(telDTO);
    }

}
