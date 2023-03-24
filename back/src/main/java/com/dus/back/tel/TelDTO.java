package com.dus.back.tel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
public class TelDTO {

    private String myPhoneNumber;
    private String phoneNumber;

    @Nullable
    private String msg;
}
