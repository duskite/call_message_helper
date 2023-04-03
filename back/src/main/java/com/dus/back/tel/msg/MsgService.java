package com.dus.back.tel.msg;

import com.dus.back.tel.TelDTO;

import java.util.List;

/**
 *  문자 수발신 처리 서비스
 */
public interface MsgService {

    /**
     * 안드로이드 단말기에 문자 발송 요청
     * 안드로이드 단에서 문자 길이에 따라 sms, mms 자동으로 변경되어 발송됨
     * @param telDTO
     */
    boolean sendSms(TelDTO telDTO);

//    /**
//     * 안드로이드 단말기에 mms 발송 요청
//     * @param telDTO
//     */
//    boolean sendMms(TelDTO telDTO);

    /**
     * 문자 대량 발송 요청
     * @param telDTOList
     * @return
     */
    boolean sendManySms(List<TelDTO> telDTOList);
}
