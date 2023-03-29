package com.dus.back.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fcm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String userId;

    @Column(unique = true)
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
}
