package com.dus.back.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String teamName;

    private String adminUserId;

    @ManyToMany
    @JoinTable(name = "team_member",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private List<Boilerplate> boilerplates = new ArrayList<>();

    public void setMember(Member member) {
        this.members.add(member);
    }

    @Builder
    public Team(String teamName, String adminUserId) {
        this.teamName = teamName;
        this.adminUserId = adminUserId;
    }
}
