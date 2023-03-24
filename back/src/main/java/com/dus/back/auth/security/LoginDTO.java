package com.dus.back.auth.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String userId;
    private String password;

    private Role role = Role.ROLE_MEMBER;
}
