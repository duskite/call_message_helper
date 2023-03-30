package com.dus.back.member;

import com.dus.back.domain.Member;
import com.dus.back.exception.DuplicateException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
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
        duplicateCheck(member);
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if(optionalMember.isPresent()){
            memberRepository.remove(optionalMember.get());
        }
    }

    @Override
    public void duplicateCheck(Member member) {
        Optional<Member> optionalMember = memberRepository.findByUserId(member.getUserId());
        if(optionalMember.isPresent()){
            throw new DuplicateException("중복 아이디");
        }
    }

    @Override
    public Member findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()){
            return optionalMember.get();
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Member findByUserId(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if(optionalMember.isPresent()){
            return optionalMember.get();
        }else {
            throw new NoSuchElementException("userId로 조회되는 유저가 없음");
        }
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
    public boolean modifyPassword(Member member) {
        Optional<Member> optionalMember = memberRepository.findByUserId(member.getUserId());
        if(optionalMember.isPresent()){
            Member findMember = optionalMember.get();
            findMember.updatePassword(member.getPassword());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean isBusinessUser(String userId) {
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if (optionalMember.isPresent()) {
            MemberType memberType = optionalMember.get().getMemberType();
            return memberType == MemberType.BUSINESS;
        }else {
            return false;
        }
    }

}
