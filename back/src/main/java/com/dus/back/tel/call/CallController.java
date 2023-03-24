package com.dus.back.tel.call;

import com.dus.back.tel.TelDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/call")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @GetMapping("/call-view")
    public String callView(){

        return "tel/call/call-form";
    }

    @PostMapping("/stop")
    public String callStop(TelDTO telDTO){

        callService.callStop(telDTO);
        return "redirect:/call/call-view";
    }

    @PostMapping("/start")
    public String callStart(TelDTO telDTO){

        callService.callStart(telDTO);
        return "redirect:/call/call-view";
    }


}
