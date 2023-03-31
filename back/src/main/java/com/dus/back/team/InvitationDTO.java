package com.dus.back.team;

import com.dus.back.domain.Invitation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationDTO {

    private String teamName;
    private String adminUserId;
    private String inviteeUserId;

    @Builder
    public Invitation toEntity() {
        return Invitation.builder().teamName(teamName).adminUserId(adminUserId).inviteeUserId(inviteeUserId).build();
    }
}
