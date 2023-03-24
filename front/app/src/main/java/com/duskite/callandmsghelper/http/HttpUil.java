package com.duskite.callandmsghelper.http;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.dto.MemberDTO;

import org.json.JSONException;

public interface HttpUil {

    void sendFcm(FcmDTO fcmDTO) throws JSONException;

    void logIn(MemberDTO memberDTO) throws JSONException;

}
