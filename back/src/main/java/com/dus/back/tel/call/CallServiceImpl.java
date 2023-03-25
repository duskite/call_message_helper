package com.dus.back.tel.call;

import com.dus.back.fcm.FcmService;
import com.dus.back.firebase.RequestFcmDTO;
import com.dus.back.firebase.RequestFcmService;
import com.dus.back.firebase.RequestFcmType;
import com.dus.back.tel.TelDTO;
import org.springframework.stereotype.Service;

@Service
public class CallServiceImpl implements CallService{

    private final RequestFcmService requestFcmService;
    private final FcmService fcmService;

    public CallServiceImpl(RequestFcmService requestFcmService, FcmService fcmService) {
        this.requestFcmService = requestFcmService;
        this.fcmService = fcmService;
    }

    @Override
    public void startCall(TelDTO telDTO) {
        RequestFcmDTO requestFcmDTO = new RequestFcmDTO();
        requestFcmDTO.setRequestFcmType(RequestFcmType.CALL_START);
        requestFcmDTO.setTelDTO(telDTO);

        String token = fcmService.findOneByPhoneNumber(telDTO.getMyPhoneNumber()).getToken();

        requestFcmService.sendFcmMessage(token, requestFcmDTO);
    }

    @Override
    public void stopCall(TelDTO telDTO) {
        RequestFcmDTO requestFcmDTO = new RequestFcmDTO();
        requestFcmDTO.setRequestFcmType(RequestFcmType.CALL_END);

        String token = fcmService.findOneByPhoneNumber(telDTO.getMyPhoneNumber()).getToken();

        requestFcmService.sendFcmMessage(token, requestFcmDTO);
    }

    @Override
    public void receiveCall(TelDTO telDTO) {

    }
}
