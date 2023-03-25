package com.duskite.callandmsghelper.http;

import android.util.Log;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.dto.LoginDTO;

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

    private static final String TAG = "HttpUtilImpl";

    private static HttpUtilImpl HttpUtilImpl;
    private OkHttpClient client;

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
    public void logIn(LoginDTO loginDTO) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", loginDTO.getUserId());
        jsonObject.put("password", loginDTO.getPassword());

        httpRequest(makeRequestBody(jsonObject), LOGIN_URL);
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
                    Log.e(TAG, "응답 실패");
                }else {
                    Log.e(TAG, "응답 성공: " + response.code());

                }
            }
        });
    }
}
