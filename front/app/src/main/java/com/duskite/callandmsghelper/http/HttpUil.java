package com.duskite.callandmsghelper.http;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.dto.LoginDTO;

import org.json.JSONException;

public interface HttpUil {

    String BASE_URL = "http://192.168.1.5:8080";
    String FCM_URL = BASE_URL + "/fcm-token";
    String LOGIN_URL = BASE_URL + "/member/login";

    /**
     * 서버에 fcm 정보 등록 요청
     * @param fcmDTO
     * @throws JSONException
     */
    void sendFcm(FcmDTO fcmDTO) throws JSONException;

    /**
     * 서버에 로그인 요청
     * @param loginDTO
     * @throws JSONException
     */
    void logIn(LoginDTO loginDTO) throws JSONException;

}
