package com.dus.back.tel.call;

import com.dus.back.tel.TelDTO;
import lombok.extern.slf4j.Slf4j;
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
    public boolean stopCall(TelDTO telDTO){

        log.error(telDTO.getMyPhoneNumber());

        callService.stopCall(telDTO);
        return true;
    }

    @PostMapping("/start")
    @ResponseBody
    public boolean startCall(TelDTO telDTO){

        log.error(telDTO.getMyPhoneNumber());

        callService.startCall(telDTO);
        return true;
    }

}
