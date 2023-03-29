package com.dus.back.member;

import com.dus.back.domain.Member;
import org.springframework.validation.Errors;

import java.util.Map;

/**
 * 회원 정보를 처리하는 서비스
 */
public interface MemberService {

    /**
     * 회원 가입
     * @param member
     * @return id
     */
    Long addMember(Member member);

    void deleteMember(String userId);

    void duplicateCheck(Member member);

    /**
     * id를 이용하여 유저 정보 조회
     * @param id
     * @return Member
     */
    Member findById(Long id);

    /**
     * 유저ID를 이용하여 유저 정보 조회
     * @param userId
     * @return
     */
    Member findByUserId(String userId);

    /**
     * 회원 가입 유효성 체크
     * @param errors
     * @return
     */
    Map<String, String> validateHandling(Errors errors);


    /**
     * 비밀번호 변경
     * @param member
     * @return
     */
    boolean modifyPassword(Member member);


}
