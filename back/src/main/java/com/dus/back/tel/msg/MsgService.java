package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;

public interface MsgService {

    void sendSms(TelDTO telDTO);
}
