package com.dus.back.firebase;

import com.dus.back.tel.TelDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestFcmDTO {

    private RequestFcmType requestFcmType;
    private TelDTO telDTO;

}
