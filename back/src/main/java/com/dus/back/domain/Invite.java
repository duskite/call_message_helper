package com.dus.back.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invite {

    private String uniqueKey;

    private String teamName;
    private String adminUserId;
    private String inviteeUserId;


    @Builder
    public Invite(String teamName, String adminUserId, String inviteeUserId) {
        this.teamName = teamName;
        this.adminUserId = adminUserId;
        this.inviteeUserId = inviteeUserId;
        this.uniqueKey = makeUniqueId(teamName, inviteeUserId);
    }

    private String makeUniqueId(String adminUserId, String inviteeUserId){
        String result = adminUserId + "_" + inviteeUserId;

        return result;
    }
}
