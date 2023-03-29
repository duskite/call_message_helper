package com.dus.back.team;

import com.dus.back.domain.Team;

import java.util.Optional;

public interface TeamRepository {

    Long save(Team group);

    void remove(Team group);

    Optional<Team> findById(Long id);
}
