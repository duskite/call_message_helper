package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.domain.Team;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface BoilerplateService {

    /**
     * 상용구 등록
     * @param boilerplate
     * @return
     */
    Long addBoilerplate(Boilerplate boilerplate);

    /**
     * 팀 상용구 등록
     * @param boilerplate
     * @return
     */
    Long addTeamBoilerplate(Boilerplate boilerplate, Team team);

    /**
     * 상용구 수정
     * @param boilerplate
     * @return
     */
    boolean modifyBoilerplate(Boilerplate boilerplate);

    /**
     * 상용구 삭제
     * @param boilerplate
     * @return
     */
    boolean deleteBoilerplate(Boilerplate boilerplate);

    /**
     * 작성자ID를 이용하여 등록된 모든 상용구 조회
     * @param authorUserId
     * @return
     */
    List<Boilerplate> findAllPersonalBoilerplate(String authorUserId);


    /**
     * 상용구 중복 체크 unique(userId, subject)
     * @param boilerplate
     */
    void duplicateCheck(Boilerplate boilerplate);

    Boilerplate findById(Long id);


}
