package com.dus.back.auth.member;

public interface MemberService {
    Long save(Member member);

    Member findById(Long id);
}
