package com.dus.back.boilerplate;

import com.dus.back.member.MemberType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.usertype.UserType;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Boilerplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String text;
    private BoilerplateType boilerplateType;
    private String authorUserId;
    @Nullable
    private String groupId;

    @Builder
    public Boilerplate(String subject, String text, BoilerplateType boilerplateType, String authorUserId, @Nullable String groupId) {
        this.subject = subject;
        this.text = text;
        this.boilerplateType = boilerplateType;
        this.authorUserId = authorUserId;
        this.groupId = groupId;
    }

}
