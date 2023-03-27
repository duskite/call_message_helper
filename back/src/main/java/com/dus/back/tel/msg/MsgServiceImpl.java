package com.dus.back.tel.msg;

import com.dus.back.fcm.FcmService;
import com.dus.back.firebase.RequestFcmDTO;
import com.dus.back.firebase.RequestFcmService;
import com.dus.back.firebase.RequestFcmType;
import com.dus.back.tel.TelDTO;
import org.springframework.stereotype.Service;

@Service
public class MsgServiceImpl implements MsgService{

    private final RequestFcmService requestFcmService;
    private final FcmService fcmService;

    public MsgServiceImpl(RequestFcmService requestFcmService, FcmService fcmService) {
        this.requestFcmService = requestFcmService;
        this.fcmService = fcmService;
    }

    @Override
    public boolean sendSms(TelDTO telDTO) {
        RequestFcmDTO requestFcmDTO = new RequestFcmDTO();
        requestFcmDTO.setRequestFcmType(RequestFcmType.SMS);
        requestFcmDTO.setTelDTO(telDTO);

        String token = fcmService.findByPhoneNumber(telDTO.getMyPhoneNumber()).getToken();
        return requestFcmService.sendFcmMessage(token, requestFcmDTO);
    }

    @Override
    public boolean sendMms(TelDTO telDTO) {
        return false;
    }

    @Override
    public void receiveSms(TelDTO telDTO) {

    }

    @Override
    public void receiveMms(TelDTO telDTO) {

    }
}
