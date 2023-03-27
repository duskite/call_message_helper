package com.dus.back.boilerplate;

import java.util.List;

public interface BoilerplateService {

    Long addBoilerplate(Boilerplate boilerplate);
    Long modifyBoilerplate(Boilerplate boilerplate);
    boolean removeBoilerplate(Boilerplate boilerplate);

    List<Boilerplate> findAllBoilerplate(String authorUserId);
}
