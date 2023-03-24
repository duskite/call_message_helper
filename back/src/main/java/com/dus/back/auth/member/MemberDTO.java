package com.dus.back.auth.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {

    private String userId;
    private String password;
    private MemberType memberType;

    public Member toEntity(){
        return Member.builder().userId(userId).password(password).memberType(memberType).build();
    }
}
