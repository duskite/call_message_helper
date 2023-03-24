package com.duskite.callandmsghelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.duskite.callandmsghelper.dto.MemberDTO;
import com.duskite.callandmsghelper.http.HttpUil;
import com.duskite.callandmsghelper.http.HttpUtilImpl;

import org.json.JSONException;

public class SignInActivity extends AppCompatActivity {

    private EditText edtUserId;
    private EditText edtPassword;
    private Button btnSignIn;
    private HttpUil httpUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        init();
    }

    private void init(){
        edtUserId = (EditText) findViewById(R.id.edtUserId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        httpUtil = HttpUtilImpl.getInstance();

        btnSignIn.setOnClickListener(setOnClickListener);
    }

    private View.OnClickListener setOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btnSignIn:
                    String userId = edtUserId.getText().toString();
                    String password = edtPassword.getText().toString();

                    MemberDTO memberDTO = new MemberDTO();
                    memberDTO.setUserId(userId);
                    memberDTO.setPassword(password);

                    try {
                        httpUtil.logIn(memberDTO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        }
    };
}
