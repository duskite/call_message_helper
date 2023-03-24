package com.dus.back.tel.call;

import com.dus.back.tel.TelDTO;

public interface CallService {

    void callStart(TelDTO telDTO);
    void callStop(TelDTO telDTO);
}
