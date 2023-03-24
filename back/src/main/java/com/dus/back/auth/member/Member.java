package com.dus.back.auth.member;

import com.dus.back.auth.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String userId, String password, String email, MemberType memberType, Role role) {

        this.userId = userId;
        this.password = password;
        this.email = email;
        this.memberType = memberType;
        this.role = role;
    }

}
