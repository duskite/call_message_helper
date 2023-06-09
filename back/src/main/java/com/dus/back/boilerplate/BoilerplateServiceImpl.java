package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import com.dus.back.exception.DuplicateException;
import com.dus.back.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.*;

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
        log.info("상용구 등록을 위해 member 조회. userId: {}", userId);
        Member findMember = memberService.findByUserId(userId);
        boilerplate.setMember(findMember);

        return boilerplateRepository.save(boilerplate);
    }

    @Override
    public Long addTeamBoilerplate(Boilerplate boilerplate, Team team) {
        duplicateCheck(boilerplate);

        String userId = boilerplate.getAuthorUserId();
        Member findMember = memberService.findByUserId(userId);
        boilerplate.setMember(findMember);
        boilerplate.setTeam(team);

        return boilerplateRepository.save(boilerplate);
    }

    @Override
    public boolean modifyBoilerplate(Boilerplate boilerplate) {
        String subject = boilerplate.getSubject();
        String userId = boilerplate.getAuthorUserId();

        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository.findBySubjectAndAuthorUserId(subject, userId);
        if (optionalBoilerplate.isPresent()) {
            Boilerplate findBoilerplate = optionalBoilerplate.get();

            findBoilerplate.update(boilerplate.getMsg());

            log.info("boilerplate modify 성공");
            return true;
        }else {
            log.info("boilerplate modify 실패");
            return false;
        }
    }

    @Override
    public boolean deleteBoilerplate(Boilerplate boilerplate) {
        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository
                .findBySubjectAndAuthorUserId(boilerplate.getSubject(), boilerplate.getAuthorUserId());

        if(optionalBoilerplate.isPresent()){
            boilerplateRepository.remove(optionalBoilerplate.get());

            return true;
        }
        return false;
    }

    @Override
    public List<Boilerplate> findAllPersonalBoilerplate(String authorUserId) {
        return boilerplateRepository.findAllPersonalByAuthorUserId(authorUserId);
    }

    @Override
    public void duplicateCheck(Boilerplate boilerplate) {
        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository.findBySubjectAndAuthorUserId(boilerplate.getSubject(), boilerplate.getAuthorUserId());
        if (optionalBoilerplate.isPresent()) {
            throw new DuplicateException("상용구 중복");
        }
    }

    @Override
    public Boilerplate findById(Long id) {
        Optional<Boilerplate> optionalBoilerplate = boilerplateRepository.findById(id);
        if(optionalBoilerplate.isPresent()){
            return optionalBoilerplate.get();
        }else {
            throw new NoSuchElementException();
        }
    }

}
