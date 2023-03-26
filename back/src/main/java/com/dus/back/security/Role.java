package com.dus.back.security;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_ADMIN("admin"), ROLE_MEMBER("user");

    private String description;

    Role(String description) {
        this.description = description;
    }
}
