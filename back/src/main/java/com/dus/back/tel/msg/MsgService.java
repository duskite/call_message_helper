package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;

/**
 *  문자 수발신 처리 서비스
 */
public interface MsgService {

    /**
     * 안드로이드 단말기에 sms 발송 요청
     * @param telDTO
     */
    void sendSms(TelDTO telDTO);
}
