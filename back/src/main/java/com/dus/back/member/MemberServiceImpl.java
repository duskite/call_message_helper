package com.dus.back.member;

import com.dus.back.fcm.FcmRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Long addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.get();
    }

    @Override
    public Member findByUserId(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        return optionalMember.get();
    }

    @Override
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validName = error.getField();
            validatorResult.put(validName, error.getDefaultMessage());
        }

        return validatorResult;
    }

    @Override
    public boolean modifyMember(Member member) {
        Optional<Member> findMember = memberRepository.findByUserId(member.getUserId());
        if(findMember.isEmpty()){
            return false;
        }else {
            findMember.get().setPassword(member.getPassword());
            return true;
        }
    }

}
