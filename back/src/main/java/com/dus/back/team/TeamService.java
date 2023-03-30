package com.dus.back.team;

import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import org.springframework.validation.Errors;

import java.util.Map;

public interface TeamService {

    Long addTeam(Team team);

    void deleteTeam(Team team);

    void duplicateCheck(Team team);

    Team findByTeamName(String teamName);

    Team findByAdminUserId(String adminUserId);

}
