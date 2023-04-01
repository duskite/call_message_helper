package com.dus.back.fcm;

import com.dus.back.domain.Fcm;

import java.util.List;
import java.util.Optional;

/**
 * 안드로이드 단말기에서 요청한
 * 휴대폰 번호와 FCM token 을 처리하는 서비스
 *
 */
public interface FcmService {

    /**
     * FCM 정보 등록
     * @param fcm
     * @return id
     */
    Long addOrModifyFcm(Fcm fcm);

    Fcm findById(Long id);

    void deleteFcm(Fcm fcm);


    /**
     * 휴대폰 번호에 대응하는 FCM 정보 조회
     * @param phoneNumber
     * @return Fcm
     */
    Fcm findByPhoneNumber(String phoneNumber);

    /**
     * 유저ID에 등록된 모든 휴대폰 번호 조회
     * @param userId
     * @return
     */
    List<String> findAllPhoneNumbersByUserId(String userId);




}
