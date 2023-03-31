package com.dus.back.team;

import com.dus.back.domain.Invite;
import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface TeamService {

    Long addTeam(Team team);

    void deleteTeam(Team team);

    void duplicateCheck(Team team);

    Team findByTeamName(String teamName);

    Team findByAdminUserId(String adminUserId);

    List<Team> findAllByAdminUserId(String adminUserId);

    /**
     * 팀 생성 유효성 체크
     * @param errors
     * @return
     */
    Map<String, String> validateHandling(Errors errors);



    /**
     * 초대장 생성
     * @param invite
     */
    void createInvite(Invite invite);

    /**
     * 팀원 초대 거절시, 초대장 삭제
     * @param invite
     */
    void rejectInvite(Invite invite);

    void acceptInvite(Invite invite);

    /**
     * 유저 ID로 모든 초대장 조히
     * @param inviteeUserId
     * @return
     */
    List<Invite> findAllInviteByInviteeUserId(String inviteeUserId);


}
