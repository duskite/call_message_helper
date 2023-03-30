package com.dus.back.tel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class TelDTO {


    private String userId;
    private String myPhoneNumber;
    private String targetPhoneNumber;

    @Nullable
    private String msg;
}
