package com.dus.back.auth.security;

import com.dus.back.auth.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


@Getter
@Setter
public class SecurityUser extends User {

    private Member member;

    public SecurityUser(Member member) {
        super(member.getUserId(), member.getPassword(), AuthorityUtils.createAuthorityList(member.getRole().toString()));

        this.member = member;
    }
}
