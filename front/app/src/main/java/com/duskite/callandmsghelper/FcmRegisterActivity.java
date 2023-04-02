package com.duskite.callandmsghelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    private String fcmToken, userId;
    private TextView tvMyPhoneNumber, tvMyUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmregister);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        init();
    }

    private void init(){
        createFcmToken();

        httpUtil = HttpUtilImpl.getInstance();

        btnRegisterFcm = (Button) findViewById(R.id.btnRegisterFcm);
        btnRegisterFcm.setOnClickListener(setOnClickListener);

        tvMyUserId = (TextView) findViewById(R.id.tvMyUserId);
        tvMyUserId.setText(userId);
        tvMyPhoneNumber = (TextView) findViewById(R.id.tvMyPhoneNumber);
        tvMyPhoneNumber.setText(getMyNumber());

    }

    private View.OnClickListener setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btnRegisterFcm:
                    if(fcmToken != null){
                        sendFcm(fcmToken);
                    }else {
                        Toast.makeText(getApplicationContext(), "아직 토큰이 발행되지 않았습니다. 잠시후 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    /**
     * fcm 토큰 발급
     */
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

    /**
     * httputil에 fcm정보 전송 요청
     * @param token
     */
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

    /**
     * 토큰, 내 번호, 유저ID를 조합하여 서버에 등록할 fcm정보 생성
     * @param token
     * @return fcm정보: FcmDTO
     * @throws IOException
     */
    private FcmDTO makeFcm(String token) throws IOException {

        FcmDTO fcmDTO = new FcmDTO();
        fcmDTO.setToken(token);
        fcmDTO.setPhoneNumber(getMyNumber());
        fcmDTO.setUserId(userId);

        return fcmDTO;
    }

    /**
     * 내 번호 가져오기
     * @return 현재 단말기 번호: String
     */
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
