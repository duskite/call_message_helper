package com.dus.back.boilerplate;

import java.util.List;
import java.util.Optional;

public interface BoilerplateRepository {

    Long save(Boilerplate boilerplate);

    void remove(Boilerplate boilerplate);

    Optional<Boilerplate> findById(Long id);

    /**
     * 작성자 아이디로 생성된 모든 상용구 찾기
     * @param authorUserId
     * @return
     */
    List<Boilerplate> findAllByAuthorUserId(String authorUserId);

    /**
     * 상용구 제목으로 찾기
     * @param subject
     * @return
     */
    Optional<Boilerplate> findBySubject(String subject);

}
