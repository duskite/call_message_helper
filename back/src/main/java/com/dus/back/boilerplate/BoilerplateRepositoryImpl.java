package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class BoilerplateRepositoryImpl implements BoilerplateRepository {

    private final EntityManager em;

    public BoilerplateRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Boilerplate boilerplate) {
        em.persist(boilerplate);
        return boilerplate.getId();
    }

    @Override
    public void remove(Boilerplate boilerplate) {
        em.remove(boilerplate);
    }

    @Override
    public Optional<Boilerplate> findById(Long id) {
        return Optional.ofNullable(em.find(Boilerplate.class, id));
    }

    @Override
    public List<Boilerplate> findAllPersonalByAuthorUserId(String authorUserId) {
        BoilerplateType boilerplateType = BoilerplateType.PERSONAL;
        List<Boilerplate> result = em.createQuery("select m from Boilerplate m where m.authorUserId =:authorUserId and m.boilerplateType =: boilerplateType", Boilerplate.class)
                .setParameter("authorUserId", authorUserId)
                .setParameter("boilerplateType", boilerplateType)
                .getResultList();
        return result;
    }

    @Override
    public Optional<Boilerplate> findBySubjectAndAuthorUserId(String subject, String authorUserId) {
        List<Boilerplate> result = em.createQuery("select m from Boilerplate m where m.subject =:subject and m.authorUserId=:authorUserId", Boilerplate.class)
                .setParameter("subject", subject)
                .setParameter("authorUserId", authorUserId)
                .getResultList();

        return result.stream().findAny();
    }
}
