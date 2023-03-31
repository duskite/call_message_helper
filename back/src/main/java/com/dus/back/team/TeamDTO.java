package com.dus.back.team;

import com.dus.back.domain.Member;
import com.dus.back.domain.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamDTO {

    private String teamName;

    private String adminUserId;


    public Team toEntity() {
        return Team.builder().teamName(teamName).adminUserId(adminUserId).build();
    }
}
