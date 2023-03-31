package com.dus.back.team;

import com.dus.back.domain.Team;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    Long save(Team team);

    void remove(Team team);

    Optional<Team> findById(Long id);

    /**
     * 관리자 id로 조회
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

    /**
     * 관리자 id로 생성된 모든 팀 조회
     * @param adminUserId
     * @return
     */
    List<Team> findAllByAdminUserId(String adminUserId);
}
