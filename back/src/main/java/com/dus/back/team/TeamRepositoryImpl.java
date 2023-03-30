package com.dus.back.team;

import com.dus.back.domain.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final EntityManager em;

    public TeamRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long save(Team team) {
        em.persist(team);
        return team.getId();
    }

    @Override
    public void remove(Team team) {
        em.remove(team);
    }

    @Override
    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    @Override
    public Optional<Team> findByAdminUserId(String adminUserId) {
        List<Team> teamList = em.createQuery("select m from Team m where m.adminUserId=:adminUserId", Team.class)
                .setParameter("adminUserId", adminUserId)
                .getResultList();

        return teamList.stream().findAny();
    }

    @Override
    public Optional<Team> findByTeamName(String teamName) {
        List<Team> teamList = em.createQuery("select m from Team m where m.teamName=:teamName", Team.class)
                .setParameter("teamName", teamName)
                .getResultList();

        return teamList.stream().findAny();
    }
}
