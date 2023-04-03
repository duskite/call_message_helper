package com.dus.back.tel.msg;

import com.dus.back.fcm.FcmService;
import com.dus.back.firebase.RequestFcmDTO;
import com.dus.back.firebase.RequestFcmService;
import com.dus.back.firebase.RequestFcmType;
import com.dus.back.tel.TelDTO;
import org.springframework.stereotype.Service;

import java.util.List;

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

        String token = fcmService.findByUserIdAndPhoneNumber(telDTO.getUserId(), telDTO.getMyPhoneNumber()).getToken();
        return requestFcmService.sendFcmMessage(token, requestFcmDTO);
    }


    @Override
    public boolean sendManySms(List<TelDTO> telDTOList) {
        if(telDTOList.isEmpty()){
            return false;
        }

        String userId = telDTOList.get(0).getUserId();
        String myPhoneNumber = telDTOList.get(0).getMyPhoneNumber();

        String token = fcmService.findByUserIdAndPhoneNumber(userId, myPhoneNumber).getToken();

        for (TelDTO telDTO : telDTOList) {
            RequestFcmDTO requestFcmDTO = new RequestFcmDTO();
            requestFcmDTO.setRequestFcmType(RequestFcmType.SMS);
            requestFcmDTO.setTelDTO(telDTO);

            requestFcmService.sendFcmMessage(token, requestFcmDTO);
        }

        return true;
    }
}
