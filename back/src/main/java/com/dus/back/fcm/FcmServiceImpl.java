package com.dus.back.fcm;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FcmServiceImpl implements FcmService {

    private final FcmRepository fcmRepository;

    public FcmServiceImpl(FcmRepository fcmRepository) {
        this.fcmRepository = fcmRepository;
    }


    @Override
    public Long addFcm(Fcm fcm) {
        if(!fcmRepository.existByPhoneNumber(fcm.getPhoneNumber())){
            return fcmRepository.save(fcm);
        }else {
            return -1L;
        }
    }


    @Override
    public Fcm findByPhoneNumber(String phoneNumber) {
        Optional<Fcm> optionalFcm = fcmRepository.findByPhoneNumber(phoneNumber);
        return optionalFcm.get();
    }

    @Override
    public List<String> findAllPhoneNumbers(String userId) {
        List<String> numbers = new ArrayList<>();

        List<Fcm> fcmList = fcmRepository.findAllByUserId(userId);
        for(Fcm fcm: fcmList){
            numbers.add(fcm.getPhoneNumber());
        }

        return numbers;
    }


}
