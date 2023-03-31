package com.dus.back.team;

import com.dus.back.domain.Invite;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InviteRepository {

    void save(Invite invite);

    void remove(Invite invite);

    /**
     * 유저 ID로 모든 초대장 조회
     * @param inviteeUserId
     * @return
     */
    List<Invite> findAllByInviteeUserId(String inviteeUserId);

}
