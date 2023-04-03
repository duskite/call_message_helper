package com.dus.back.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(
        name = "userIdAndPhoneNumber", columnNames = {"userId", "phoneNumber"}
))
public class Fcm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String userId;
    private String phoneNumber;
    private String token;

    public void setMember(Member member) {
        this.member = member;
        member.getFcms().add(this);
    }

    @Builder
    public Fcm(String phoneNumber, String token, String userId) {
        this.phoneNumber = phoneNumber;
        this.token = token;
        this.userId = userId;
    }

    public Fcm updateToken(String token) {
        this.token = token;

        return this;
    }
}
