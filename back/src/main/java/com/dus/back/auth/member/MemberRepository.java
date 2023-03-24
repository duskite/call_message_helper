package com.dus.back.auth.member;

public interface MemberRepository {

    Long save(Member member);

    Member findById(Long id);
}
