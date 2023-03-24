package com.dus.back.auth.fcm;

import java.util.Optional;

public interface FcmRepository {

    Long save(Fcm fcm);

    Optional<Fcm> findByPhoneNumber(String phoneNumber);
}
