package com.dus.back.fcm;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Fcm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String phoneNumber;
    private String token;

    @Builder
    public Fcm(String userId, String phoneNumber, String token) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.token = token;
    }

}
