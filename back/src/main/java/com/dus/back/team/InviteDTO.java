package com.dus.back.team;

import com.dus.back.domain.Invite;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteDTO {

    private String teamName;
    private String adminUserId;
    private String inviteeUserId;

    @Builder
    public Invite toEntity() {
        return Invite.builder().teamName(teamName).adminUserId(adminUserId).inviteeUserId(inviteeUserId).build();
    }
}
