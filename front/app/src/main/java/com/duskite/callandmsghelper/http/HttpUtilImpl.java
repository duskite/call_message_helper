package com.duskite.callandmsghelper.http;

import android.util.Log;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.dto.MemberDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtilImpl implements HttpUil{

    private static HttpUtilImpl HttpUtilImpl;
    private OkHttpClient client;
    private final String BASE_URL = "http://192.168.1.7:8080/";
    private final String FCM_URL = BASE_URL + "auth/fcm-token/";

    private HttpUtilImpl(){
        client = new OkHttpClient();
    }

    public static HttpUtilImpl getInstance(){
        if(HttpUtilImpl == null){
            HttpUtilImpl = new HttpUtilImpl();
        }
        return HttpUtilImpl;
    }

    @Override
    public void sendFcm(FcmDTO fcmDTO) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", fcmDTO.getUserId());
        jsonObject.put("phoneNumber", fcmDTO.getPhoneNumber());
        jsonObject.put("token", fcmDTO.getToken());

        httpRequest(makeRequestBody(jsonObject), FCM_URL);
    }

    @Override
    public void logIn(MemberDTO memberDTO) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", memberDTO.getUserId());
        jsonObject.put("password", memberDTO.getPassword());

//        httpRequest(makeRequestBody(jsonObject), FCM_URL);
    }

    private RequestBody makeRequestBody(JSONObject jsonObject){
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        return body;
    }

    private void httpRequest(RequestBody body, String url){
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(!response.isSuccessful()){
                    Log.e("http요청", "실패");
                }else {
                    Log.e("http요청", "성공");
                }
            }
        });
    }
}
