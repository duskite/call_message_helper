package com.dus.back.team;

import com.dus.back.domain.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager em;

    public TeamRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Team group) {
        em.persist(group);
        return group.getId();
    }

    @Override
    public void remove(Team group) {
        em.remove(group);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }
}
