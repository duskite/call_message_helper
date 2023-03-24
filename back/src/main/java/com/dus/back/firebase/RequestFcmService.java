package com.dus.back.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.dus.back.firebase.RequestFcmType.*;

@Service
@Slf4j
public class RequestFcmService {

    public void sendFcmMessage(String token, RequestFcmDTO requestFcmDTO) {

        Message message = null;
        switch (requestFcmDTO.getRequestFcmType()){
            case SMS:
            case MMS:
                message = makeFcmSmsMessage(token,
                        requestFcmDTO.getRequestFcmType(),
                        requestFcmDTO.getTelDTO().getPhoneNumber(),
                        requestFcmDTO.getTelDTO().getMsg());
                break;

            case CALL_START:
                message = makeFcmCallMessage(token,
                        requestFcmDTO.getRequestFcmType(),
                        requestFcmDTO.getTelDTO().getPhoneNumber());
                break;
            case CALL_END:
                message = makeFcmCallStopMessage(token,
                        requestFcmDTO.getRequestFcmType());
                break;
        }

        try{
            String response = FirebaseMessaging.getInstance().send(message);

            log.info(response);
        }catch (FirebaseMessagingException e){
            log.error("FCM 메세지 전송중 오류");
        }
    }


    private Message makeFcmSmsMessage(String token, RequestFcmType requestFcmType, String phoneNumber, String msg) {
        Message message = Message.builder()
                .setToken(token)
                .putData("phoneNumber", phoneNumber)
                .putData("msg", msg)
                .putData("requestFcmType", requestFcmType.name())
                .build();

        return message;
    }

    private Message makeFcmCallMessage(String token, RequestFcmType requestFcmType, String phoneNumber){
        Message message = Message.builder()
                .setToken(token)
                .putData("phoneNumber",phoneNumber)
                .putData("requestFcmType", requestFcmType.name())
                .build();

        return message;
    }

    private Message makeFcmCallStopMessage(String token, RequestFcmType requestFcmType){
        Message message = Message.builder()
                .setToken(token)
                .putData("requestFcmType", requestFcmType.name())
                .build();

        return message;
    }

}
