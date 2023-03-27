package com.dus.back.boilerplate;

import com.dus.back.member.MemberType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.usertype.UserType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Boilerplate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private BoilerplateType boilerplateType;
    private String authUserId;

    private String subscribeUserId;

}
