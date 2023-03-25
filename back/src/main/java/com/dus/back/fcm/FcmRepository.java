package com.dus.back.fcm;

import java.util.List;
import java.util.Optional;

public interface FcmRepository {

    /**
     * FCM 정보 등록
     * @param fcm
     * @return id
     */
    Long save(Fcm fcm);


    /**
     * 휴대폰 번호에 대응하는 FCM 정보 조회
     * @param phoneNumber
     * @return Optional<Fcm>
     */
    Optional<Fcm> findByPhoneNumber(String phoneNumber);


    /**
     * 유저ID에 등록된 모든 FCM 정보 조회
     * @param userId
     * @return
     */
    List<Fcm> findAllByUserId(String userId);

    boolean existByPhoneNumber(String phoneNumber);
}
