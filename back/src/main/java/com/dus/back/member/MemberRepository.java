package com.dus.back.member;

import com.dus.back.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    /**
     * 회원 가입
     * @param member
     * @return id
     */
    Long save(Member member);

    void remove(Member member);

    /**
     * id를 이용하여 유저 정보 조회
     * @param id
     * @return Member
     */
    Optional<Member> findById(Long id);

    /**
     * 유저ID를 이용하여 유저 정보 조회
     * @param userId
     * @return
     */
    Optional<Member> findByUserId(String userId);

}
