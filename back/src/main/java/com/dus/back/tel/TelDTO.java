package com.dus.back.tel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
public class TelDTO {


    private String userId;
    private String myPhoneNumber;
    private String targetPhoneNumber;

    @Nullable
    private String msg;
}
