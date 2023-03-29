package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Member;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BoilerplateServiceImpl implements BoilerplateService {

    private final BoilerplateRepository boilerplateRepository;
    private final MemberService memberService;

    public BoilerplateServiceImpl(BoilerplateRepository boilerplateRepository, MemberService memberService) {
        this.boilerplateRepository = boilerplateRepository;
        this.memberService = memberService;
    }

    @Override
    public Long addBoilerplate(Boilerplate boilerplate) {
        duplicateCheck(boilerplate);

        String userId = boilerplate.getAuthorUserId();
        log.info("fcm 등록을 위해 member 조회. userId: {}", userId);
        Member findMember = memberService.findByUserId(userId);
        boilerplate.setMember(findMember);

        return boilerplateRepository.save(boilerplate);
    }



    @Override
    public Long modifyBoilerplate(Boilerplate boilerplate) {
        return null;
    }

    @Override
    public boolean removeBoilerplate(Boilerplate boilerplate) {
        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository
                .findBySubjectAndAuthorUserId(boilerplate.getSubject(), boilerplate.getAuthorUserId());

        if(optionalBoilerplate.isPresent()){
            boilerplateRepository.remove(optionalBoilerplate.get());

            return true;
        }
        return false;
    }

    @Override
    public List<Boilerplate> findAllBoilerplate(String authorUserId) {
        return boilerplateRepository.findAllByAuthorUserId(authorUserId);
    }

    @Override
    public void duplicateCheck(Boilerplate boilerplate) {
        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository.findBySubjectAndAuthorUserId(boilerplate.getSubject(), boilerplate.getAuthorUserId());
        if (optionalBoilerplate.isPresent()) {
            throw new DuplicateException("상용구 중복");
        }
    }
}
