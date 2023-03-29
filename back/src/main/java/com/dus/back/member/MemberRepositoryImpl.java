package com.dus.back.member;

import com.dus.back.domain.Member;
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
    public void remove(Member member) {
        em.remove(member);
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

}
