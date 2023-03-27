package com.duskite.callandmsghelper.http;

import android.util.Log;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.dto.LoginDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtilImpl implements HttpUil{

    private static final String TAG = "HttpUtilImpl";

    private static HttpUtilImpl HttpUtilImpl;
    private OnLoginResultListener onLoginResultListener;
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
    public void sendFcm(FcmDTO fcmDTO){

        try{
            Request request = makeRequest(makeFcmBody(fcmDTO), FCM_URL);
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(!response.isSuccessful()){
                        Log.d(TAG, "응답 실패");
                    }else {
                        Log.d(TAG, "응답 성공: " + response.code());
                    }
                }
            });
        }catch (JSONException e){

        }

    }

    @Override
    public void logIn(LoginDTO loginDTO) {

        Request request = makeRequest(makeLoginBody(loginDTO), LOGIN_URL);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Log.d(TAG, "응답 실패");
                }else {
                    Log.d(TAG, "응답 성공: " + response.code());
                    String userId = response.header("login-userId");
                    String errorMessage = response.header("login-error");
                    if(userId != null){
                        Log.d(TAG, userId);
                        onLoginResultListener.loginResult(userId, null);
                    }else {
                        errorMessage = URLDecoder.decode(errorMessage, "UTF-8");
                        Log.d(TAG, errorMessage);
                        onLoginResultListener.loginResult(userId, errorMessage);
                    }
                }

            }
        });
    }

    @Override
    public void setOnLoginResultListener(OnLoginResultListener onLoginResultListener) {
        this.onLoginResultListener = onLoginResultListener;
    }


    private RequestBody makeFcmBody(FcmDTO fcmDTO) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", fcmDTO.getUserId());
        jsonObject.put("phoneNumber", fcmDTO.getPhoneNumber());
        jsonObject.put("token", fcmDTO.getToken());

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        return body;
    }

    private RequestBody makeLoginBody(LoginDTO loginDTO){
        RequestBody body = new FormBody.Builder()
                .add("userId", loginDTO.getUserId())
                .add("password", loginDTO.getPassword())
                .add("android", "android")
                .build();

        return body;
    }

    private Request makeRequest(RequestBody body, String url) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return request;
    }

}
