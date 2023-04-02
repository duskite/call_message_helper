package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
public class BoilerplateDTO {

    private String subject;
    private String msg;
    private BoilerplateType boilerplateType;
    private String authorUserId;

    private String teamName;

    public Boilerplate toEntity(){
        return Boilerplate.builder().subject(subject).msg(msg).boilerplateType(boilerplateType).authorUserId(authorUserId).build();
    }
}
