package com.dus.back.domain;

import com.dus.back.boilerplate.BoilerplateType;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
        name = "AuthorUserIdAndSubject", columnNames = {"authorUserId", "subject"}
))
public class Boilerplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String msg;

    private String authorUserId;

    @Enumerated(EnumType.STRING)
    private BoilerplateType boilerplateType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Boilerplate update(String subject, String msg) {
        this.subject = subject;
        this.msg = msg;

        return this;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getBoilerplates().add(this);
    }

    @Builder
    public Boilerplate(String subject, String msg, BoilerplateType boilerplateType, String authorUserId) {
        this.subject = subject;
        this.msg = msg;
        this.boilerplateType = boilerplateType;
        this.authorUserId = authorUserId;
    }
}
