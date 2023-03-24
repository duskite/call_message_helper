package com.duskite.callandmsghelper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.duskite.callandmsghelper.dto.FcmDTO;
import com.duskite.callandmsghelper.http.HttpUil;
import com.duskite.callandmsghelper.http.HttpUtilImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.io.IOException;

public class FcmRegisterActivity extends AppCompatActivity {

    private Button btnRegisterFcm;
    private HttpUil httpUtil;
    private String fcmToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmregister);

        init();
    }

    private void init(){
        createFcmToken();

        httpUtil = HttpUtilImpl.getInstance();

        btnRegisterFcm = (Button) findViewById(R.id.btnRegisterFcm);
        btnRegisterFcm.setOnClickListener(setOnClickListener);

    }

    private View.OnClickListener setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btnRegisterFcm:
                    if(fcmToken != null){
                        sendFcm(fcmToken);
                    }
                    break;
            }
        }
    };

    private void createFcmToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("FCM 토큰", "토큰 발행 실패");

                    Toast.makeText(getApplicationContext(), "토큰 발행 실패 - 인터넷 연결 확인 필요", Toast.LENGTH_LONG).show();
                    return;
                }

                fcmToken = task.getResult();
            }
        });
    }

    private void sendFcm(String token){
        try{
            if(token != null){
                try {
                    httpUtil.sendFcm(makeFcm(token));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException e){
            Log.e("FCM 토큰", "서버로 전송 오류");
        }
    }

    private FcmDTO makeFcm(String token) throws IOException {

        FcmDTO fcmDTO = new FcmDTO();
        fcmDTO.setToken(token);
        fcmDTO.setPhoneNumber(getMyNumber());
        fcmDTO.setUserId("ysy5593");

        return fcmDTO;
    }

    private String getMyNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return telephonyManager.getLine1Number().replace("+82", "0");
    }
}
