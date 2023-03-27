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
    boolean sendSms(TelDTO telDTO);

    /**
     * 안드로이드 단말기에 mms 발송 요청
     * @param telDTO
     */
    boolean sendMms(TelDTO telDTO);

    /**
     * 현재 단말기에서 받은 sms 를 수신
     * @param telDTO
     */
    void receiveSms(TelDTO telDTO);

    /**
     * 현재 단말기에서 받은 mms 를 수신
     * @param telDTO
     */
    void receiveMms(TelDTO telDTO);
}
