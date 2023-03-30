package com.dus.back.fcm;

import com.dus.back.domain.Fcm;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FcmDTO {

    private String userId;
    private String phoneNumber;
    private String token;

    public Fcm toEntity(){
        return Fcm.builder().phoneNumber(phoneNumber).token(token).userId(userId).build();
    }
}
