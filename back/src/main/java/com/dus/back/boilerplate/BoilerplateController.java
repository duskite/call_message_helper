package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import com.dus.back.vaild.CheckBoilerplateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class BoilerplateController {

    private final BoilerplateService boilerplateService;
    private final CheckBoilerplateValidator checkBoilerplateValidator;

    public BoilerplateController(BoilerplateService boilerplateService, CheckBoilerplateValidator checkBoilerplateValidator) {
        this.boilerplateService = boilerplateService;
        this.checkBoilerplateValidator = checkBoilerplateValidator;
    }

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(checkBoilerplateValidator);
    }


    @GetMapping("/boilerplate-page/{userId}")
    public String boilerplatePage(Model model, Authentication authentication, @PathVariable("userId") String userId) {

        if(!userId.equals(authentication.getName())){
            return "forbidden";
        }
        setBaseModelInfo(model, authentication);

        return "/boilerplate/boilerplate-page";
    }

    /**
     * 비동기 요청 후 list 부분을 내려주는데, error 발생시 현재 방법(뷰 리졸버 - 타임리프 템플릿 엔진 사용)으로는 클라이언트에 적절한 응답을 내려주기 힘듦
     * 별도로 헤더에 에러를 담아서 응답하는 것으로 처리(true:String)
     * @param httpServletResponse
     */
    private void customErrorResponse(HttpServletResponse httpServletResponse){
        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpServletResponse.setHeader("duplicate-error", "true");
    }

    @PostMapping("/boilerplate")
    public String boilerplateAdd(@Valid BoilerplateDTO boilerplateDTO, Errors errors, Model model,
                                 Authentication authentication, HttpServletResponse httpServletResponse) {

        log.info("boilerplate 등록 요청");

        if (errors.hasErrors()) {
            log.error("상용구 중복 등록 에러 발생");
            customErrorResponse(httpServletResponse);

            return "/fragments/boilerplate/boilerplate-list";
        }

        boilerplateService.addBoilerplate(boilerplateDTO.toEntity());

        setBaseModelInfo(model, authentication);

        return "/fragments/boilerplate/boilerplate-list";
    }

    @PutMapping("/boilerplate")
    public String boilerplateUpdate(Model model, BoilerplateDTO boilerplateDTO, Authentication authentication) {
        log.info("boilerplate 수정 요청");

        boilerplateService.modifyBoilerplate(boilerplateDTO.toEntity());
        log.info("수정 요청 온 DTO: {}", boilerplateDTO.toString());

        setBaseModelInfo(model, authentication);

        return "/fragments/boilerplate/boilerplate-list";

    }

    @DeleteMapping("/boilerplate")
    public String boilerplateDelete(Model model, BoilerplateDTO boilerplateDTO, Authentication authentication) {
        log.info("boilerplate 삭제 요청");

        boilerplateService.deleteBoilerplate(boilerplateDTO.toEntity());
        setBaseModelInfo(model, authentication);

        return "/fragments/boilerplate/boilerplate-list";
    }

    /**
     * 상용구 화면에 필요한 기본 정보를 구성함
     * @param model
     * @param authentication
     */
    private void setBaseModelInfo(Model model, Authentication authentication) {

        List<Boilerplate> boilerplateList = boilerplateService.findAllBoilerplate(authentication.getName());

        model.addAttribute("boilerplateList", boilerplateList);
        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);
    }
}
