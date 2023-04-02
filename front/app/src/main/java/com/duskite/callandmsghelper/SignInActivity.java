package com.duskite.callandmsghelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.duskite.callandmsghelper.dto.LoginDTO;
import com.duskite.callandmsghelper.http.HttpUil;
import com.duskite.callandmsghelper.http.HttpUtilImpl;
import com.duskite.callandmsghelper.http.OnLoginResultListener;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

public class SignInActivity extends AppCompatActivity {

    private static String TAG = "SignInActivity";

    private TextInputEditText edtUserId, edtPassword;
    private Button btnSignIn;
    private HttpUil httpUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }

    private void init(){
        edtUserId = (TextInputEditText) findViewById(R.id.edtUserId);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        httpUtil = HttpUtilImpl.getInstance();
        httpUtil.setOnLoginResultListener(new OnLoginResultListener() {
            @Override
            public void loginResult(String userId, String errorMessage) {
                if(userId != null){
                    Intent intent = new Intent(getApplicationContext(), FcmRegisterActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }else {
                    Log.e(TAG, errorMessage);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }, 0);
                }

            }
        });

        btnSignIn.setOnClickListener(setOnClickListener);
    }

    private View.OnClickListener setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btnSignIn:
                    String userId = edtUserId.getText().toString();
                    String password = edtPassword.getText().toString();

                    LoginDTO loginDTO = new LoginDTO();
                    loginDTO.setUserId(userId);
                    loginDTO.setPassword(password);

                    Log.d(TAG, loginDTO.getUserId());
                    Log.d(TAG, loginDTO.getPassword());


                    try {
                        httpUtil.logIn(loginDTO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    break;
            }
        }
    };
}
