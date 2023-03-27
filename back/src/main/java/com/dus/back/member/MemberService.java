package com.dus.back.member;

import org.springframework.validation.Errors;

import java.util.List;
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
    Long save(Member member);

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
     * 회원정보 변경
     * @param member
     * @return
     */
    boolean update(Member member);


}
