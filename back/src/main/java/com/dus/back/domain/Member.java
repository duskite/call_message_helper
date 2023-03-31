package com.dus.back.domain;

import com.dus.back.member.MemberType;
import com.dus.back.security.Role;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Fcm> fcms = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Boilerplate> boilerplates = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    private List<Team> teams = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private Role role;


    public Member updatePassword(String password) {
        this.password = password;

        return this;
    }

    public Member updateMemberType(MemberType memberType) {
        this.memberType = memberType;

        return this;
    }

    @Builder
    public Member(String userId, String password, String email, MemberType memberType, Role role) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.memberType = memberType;
        this.role = role;
    }
}
