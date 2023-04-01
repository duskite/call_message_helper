package com.dus.back.fcm;

import com.dus.back.domain.Fcm;
import com.dus.back.domain.Member;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberRepository;
import com.dus.back.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class FcmServiceImpl implements FcmService {

    private final FcmRepository fcmRepository;
    private final MemberService memberService;

    public FcmServiceImpl(FcmRepository fcmRepository, MemberService memberService) {
        this.fcmRepository = fcmRepository;
        this.memberService = memberService;
    }


    @Override
    public Long addOrModifyFcm(Fcm fcm) {

        Optional<Fcm> optionalFcm = fcmRepository.findByPhoneNumber(fcm.getPhoneNumber());
        if(optionalFcm.isPresent()){
            Fcm findFcm = optionalFcm.get();
            findFcm.updateToken(fcm.getToken());

            return findFcm.getId();
        }

        String userId = fcm.getUserId();
        log.info("fcm 등록을 위해 member 조회. userId: {}", userId);
        Member findMember = memberService.findByUserId(userId);
        fcm.setMember(findMember);

        return fcmRepository.save(fcm);
    }

    @Override
    public Fcm findById(Long id) {
        Optional<Fcm> optionalFcm = fcmRepository.findById(id);
        return optionalFcm.get();
    }

    @Override
    public void deleteFcm(Fcm fcm) {
        Optional<Fcm> optionalFcm = fcmRepository.findByPhoneNumber(fcm.getPhoneNumber());
        if(optionalFcm.isPresent()){
            fcmRepository.remove(optionalFcm.get());
        }
    }

    @Override
    public Fcm findByPhoneNumber(String phoneNumber) {
        Optional<Fcm> optionalFcm = fcmRepository.findByPhoneNumber(phoneNumber);
        if(optionalFcm.isPresent()){
            return optionalFcm.get();
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<String> findAllPhoneNumbersByUserId(String userId) {
        List<String> numbers = new ArrayList<>();

        List<Fcm> fcmList = fcmRepository.findAllByUserId(userId);
        for(Fcm fcm: fcmList){
            numbers.add(fcm.getPhoneNumber());
        }

        return numbers;
    }

}
