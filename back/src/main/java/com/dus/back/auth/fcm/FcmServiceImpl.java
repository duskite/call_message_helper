package com.dus.back.auth.fcm;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class FcmServiceImpl implements FcmService {

    private final FcmRepository fcmRepository;

    public FcmServiceImpl(FcmRepository fcmRepository) {
        this.fcmRepository = fcmRepository;
    }


    @Override
    public Long save(Fcm fcm) {
        return fcmRepository.save(fcm);
    }


    @Override
    public Fcm findOneByPhoneNumber(String phoneNumber) {
        Optional<Fcm> optionalFcm = fcmRepository.findByPhoneNumber(phoneNumber);
        return optionalFcm.get();
    }


}