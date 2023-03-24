package com.dus.back.auth.member;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }
}
