package com.dus.back.team;

import com.dus.back.domain.Invitation;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InvitationRepository {

    void save(Invitation invite);

    void remove(Invitation invite);

    /**
     * 유저 ID로 모든 초대장 조회
     * @param inviteeUserId
     * @return
     */
    List<Invitation> findAllByInviteeUserId(String inviteeUserId);

}
