package com.dus.back.fcm;

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

    @Override
    public List<Fcm> findAllByUserId(String userId) {

        List<Fcm> result = em.createQuery("select m from Fcm m where m.userId =:userId", Fcm.class)
                .setParameter("userId", userId)
                .getResultList();
        return result;
    }

    @Override
    public boolean existByPhoneNumber(String phoneNumber) {
        List<Fcm> fcms = em.createQuery("select m from Fcm m where m.phoneNumber=:phoneNumber", Fcm.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();

        if(fcms.size() == 0){
            return false;
        }

        return true;
    }
}
