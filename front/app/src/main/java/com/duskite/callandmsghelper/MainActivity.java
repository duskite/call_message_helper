package com.duskite.callandmsghelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.duskite.callandmsghelper.tel.FcmService;
import com.duskite.callandmsghelper.tel.MsgBroadcastReceiver;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private final String[] PERMISSIONS = {
            android.Manifest.permission.READ_PHONE_NUMBERS,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.ANSWER_PHONE_CALLS,
            android.Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS
    };

    private Button btnSignInActivity, btnRegisterFcmActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        FirebaseApp.initializeApp(getApplicationContext());

        getPermission(PERMISSIONS);

        btnSignInActivity = (Button) findViewById(R.id.btnSignInActivity);
        btnRegisterFcmActivity = (Button) findViewById(R.id.btnRegisterFcmActivity);
        btnSignInActivity.setOnClickListener(setOnClickListener);
        btnRegisterFcmActivity.setOnClickListener(setOnClickListener);

        Intent intent = new Intent(this, FcmService.class);
        startService(intent);

        BroadcastReceiver broadcastReceiver = new MsgBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }
    private void getPermission(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1000);
    }

    private View.OnClickListener setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btnSignInActivity:
                    Intent signin = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signin);
                    break;

                case R.id.btnRegisterFcmActivity:
                    Intent fcm = new Intent(getApplicationContext(), FcmRegisterActivity.class);
                    startActivity(fcm);
                    break;
            }
        }
    };

}