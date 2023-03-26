package com.dus.back.fcm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FcmController {

    private final FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/fcm-token")
    @ResponseBody
    public Long saveFcm(@RequestBody FcmDTO fcmDTO){

        return fcmService.save(fcmDTO.toEntity());
    }
}
