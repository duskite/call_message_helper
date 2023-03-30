package com.dus.back.team;

import com.dus.back.domain.Team;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TeamRepository {

    Long save(Team team);

    void remove(Team team);

    Optional<Team> findById(Long id);

    /**
     * 팀을 생성한 어드민id로 조회
     * @param adminUserId
     * @return
     */
    Optional<Team> findByAdminUserId(String adminUserId);

    /**
     * 팀 이름으로 조회
     * @param teamName
     * @return
     */
    Optional<Team> findByTeamName(String teamName);
}
