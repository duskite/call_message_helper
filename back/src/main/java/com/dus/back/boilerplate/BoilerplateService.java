package com.dus.back.boilerplate;

import java.util.List;

public interface BoilerplateService {

    BoilerplateDTO createBoilerplate();
    BoilerplateDTO updateBoilerplate();
    boolean deleteBoilerplate();

    List<BoilerplateDTO> loadAllBoilerplate();
}
