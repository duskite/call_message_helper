package com.dus.back.boilerplate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class BoilerplateDTO {

    private String subject;
    private String text;
    private BoilerplateType boilerplateType;
    private String authorUserId;
    @Nullable
    private String groupId;

    public Boilerplate toEntity(){
        return Boilerplate.builder().subject(subject).text(text).boilerplateType(boilerplateType).authorUserId(authorUserId).groupId(groupId).build();
    }
}
