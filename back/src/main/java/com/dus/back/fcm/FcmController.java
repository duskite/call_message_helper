package com.dus.back.fcm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class FcmController {

    private final FcmService fcmService;

    public FcmController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/fcm-token")
    @ResponseBody
    public Long fcmAdd(@RequestBody FcmDTO fcmDTO){
        log.info("FCM 등록 요청. userId: {}", fcmDTO.getUserId());
        log.info("FCM 등록 요청. phoneNumber: {}", fcmDTO.getPhoneNumber());

        return fcmService.addFcm(fcmDTO.toEntity());
    }
}
