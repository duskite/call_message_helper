package com.duskite.callandmsghelper.http;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.dto.LoginDTO;

import org.json.JSONException;

public interface HttpUil {

    String BASE_URL = "http://20.196.200.38";
    String FCM_URL = BASE_URL + "/fcm-token";
    String LOGIN_URL = BASE_URL + "/member/login";

    /**
     * 서버에 fcm 정보 등록 요청
     * @param fcmDTO
     * @throws JSONException
     */
    void sendFcm(FcmDTO fcmDTO) throws JSONException;

    /**
     * 서버에 로그인 요청 - 로그인은 fcm정보 등록을 위해서임. 다른 동작과는 무관함
     * @param loginDTO
     * @throws JSONException
     */
    void logIn(LoginDTO loginDTO) throws JSONException;

    /**
     * 로그인 결과 반환 리스너
     * @param onLoginResultListener
     */
    void setOnLoginResultListener(OnLoginResultListener onLoginResultListener);

}
