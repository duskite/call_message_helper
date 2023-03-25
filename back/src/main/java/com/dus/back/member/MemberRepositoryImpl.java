package com.dus.back.member;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

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
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        List<Member> members = em.createQuery("select m from Member m where m.userId=:userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();

        return members.stream().findAny();
    }

    @Override
    public boolean existByUserId(String userId) {
        List<Member> members = em.createQuery("select m from Member m where m.userId=:userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();

        if(members.size() == 0){
            return false;
        }

        return true;
    }
}
