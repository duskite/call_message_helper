package com.dus.back.auth.fcm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmDTO {

    private String userId;
    private String phoneNumber;
    private String token;

    public Fcm toEntity(){
        return Fcm.builder().userId(userId).phoneNumber(phoneNumber).token(token).build();
    }
}
