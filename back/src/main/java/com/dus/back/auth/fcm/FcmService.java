package com.dus.back.auth.fcm;

public interface FcmService {

    Long save(Fcm fcm);
    Fcm findOneByPhoneNumber(String phoneNumber);

}
