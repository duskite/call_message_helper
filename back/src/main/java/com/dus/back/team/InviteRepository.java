package com.dus.back.team;

import com.dus.back.domain.Invite;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface InviteRepository {

    void save(Invite invite);

    void remove(Invite invite);

    Optional<Invite> findByInviteeUserId(String inviteeUserId);

    Optional<Invite> findByAdminUserId(String adminUserId);

}
