package com.dus.back.member;

import com.dus.back.domain.Member;
import com.dus.back.security.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class MemberDTO {

    @NotBlank
    private String userId;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "아이디, 비밀번호 분실시 e-mail 이 필요합니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    private MemberType memberType;
    private Role role = Role.ROLE_MEMBER;

    public Member toEntity(){
        return Member.builder().userId(userId).email(email).password(password).memberType(memberType).role(role).build();
    }
}
