package com.dus.back.tel.call;

import com.dus.back.tel.TelDTO;

/**
 * 통화 수발신 처리 서비스
 */
public interface CallService {

    /**
     * 안드로이드 단말기에 통화 시작 요청
     * @param telDTO
     */
    boolean startCall(TelDTO telDTO);

    /**
     * 안드로이드 단말기에 통화 종료 요청
     * @param telDTO
     */
    boolean stopCall(TelDTO telDTO);


    /**
     * 현재 단말기에 걸려온 통화를 수신
     * @param telDTO
     */
    void receiveCall(TelDTO telDTO);


}
