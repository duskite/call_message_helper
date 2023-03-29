package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;

import java.util.List;

public interface BoilerplateService {

    /**
     * 상용구 등록
     * @param boilerplate
     * @return
     */
    Long addBoilerplate(Boilerplate boilerplate);

    /**
     * 상용구 수정
     * @param boilerplate
     * @return
     */
    Long modifyBoilerplate(Boilerplate boilerplate);

    /**
     * 상용구 삭제
     * @param boilerplate
     * @return
     */
    boolean removeBoilerplate(Boilerplate boilerplate);

    /**
     * 작성자ID를 이용하여 등록된 모든 상용구 조회
     * @param authorUserId
     * @return
     */
    List<Boilerplate> findAllBoilerplate(String authorUserId);
}