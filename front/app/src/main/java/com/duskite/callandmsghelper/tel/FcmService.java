package com.duskite.callandmsghelper.tel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

public class FcmService extends FirebaseMessagingService {

    private static String TAG = "FcmService";

    /**
     * 서버로부터 받은 메세지 타입에 따라 단말기 동작 분기 처리
     * @param message Remote message that has been received.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        Map<String, String> fcmData = message.getData();
        String fcmType = fcmData.get("requestFcmType");
        if(fcmType.equals("STAND_BY")){
            Log.d(TAG, "FCM 수신 대기 요청");
            return;
        }

        String phoneNumber = fcmData.get("targetPhoneNumber");

        Log.d(TAG, "넘어온 번호:" + phoneNumber);

        if(fcmType.equals("SMS")){
            String msg = fcmData.get("msg");
            sendSMS(phoneNumber, msg);
        } else if (fcmType.equals("MMS")) {
            String msg = fcmData.get("msg");
            sendMMS(phoneNumber, msg);
        } else if (fcmType.equals("CALL_START")) {
            callStart(phoneNumber);
        } else if(fcmType.equals("CALL_END")){
            callEnd();
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private void sendSMS(String number, String msg){

        Log.d(TAG, "넘어온 번호:" + number);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, msg, null, null);
    }

    private void sendMMS(String number, String msg){
        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> partMassage = smsManager.divideMessage(msg);
        smsManager.sendMultipartTextMessage(number, null, partMassage, null, null);
    }

    private void callStart(String number) {
        number.replace("010", "+8210");
        String phoneNum = "tel:" + number;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNum));
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void callEnd(){
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            telecomManager.endCall();
        }
    }
}
