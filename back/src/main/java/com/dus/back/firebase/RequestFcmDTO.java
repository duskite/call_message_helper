package com.dus.back.firebase;

import com.dus.back.tel.TelDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestFcmDTO {

    private RequestFcmType requestFcmType;
    private TelDTO telDTO;

}
