package com.dus.back.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;

    private String admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getTeams().add(this);
    }

    @Builder
    public Team(String teamName) {
        this.teamName = teamName;
    }
}
