package com.dus.back.team;

import com.dus.back.domain.Invitation;
import com.dus.back.domain.Team;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface TeamService {

    Long addTeam(Team team);

    boolean deleteTeam(Team team);

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
     * 팀원 삭제
     * @param teamName
     * @param userID
     */
    void deleteTeamMember(String teamName, String userID);

    /**
     * 초대장 생성
     * @param invite
     */
    boolean createInvite(Invitation invite);

    /**
     * 팀원 초대 거절시, 초대장 삭제
     * @param invite
     */
    void rejectInvite(Invitation invite);

    /**
     * 팀원 초대 수락시
     * @param invite
     */
    void acceptInvite(Invitation invite);

    /**
     * 유저 ID로 모든 초대장 조히
     * @param inviteeUserId
     * @return
     */
    List<Invitation> findAllInviteByInviteeUserId(String inviteeUserId);


}
