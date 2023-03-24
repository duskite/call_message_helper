package com.dus.back.auth.fcm;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class FcmRepositoryImpl implements FcmRepository{

    private final EntityManager em;

    public FcmRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Fcm fcm) {
        em.persist(fcm);
        return fcm.getId();
    }

    @Override
    public Optional<Fcm> findByPhoneNumber(String phoneNumber) {
        List<Fcm> result = em.createQuery("select m from Fcm m where m.phoneNumber =:phoneNumber", Fcm.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();

        return result.stream().findAny();
    }

}
