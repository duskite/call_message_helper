package com.dus.back.boilerplate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BoilerplateController {

    private final BoilerplateService boilerplateService;

    public BoilerplateController(BoilerplateService boilerplateService) {
        this.boilerplateService = boilerplateService;
    }

    @GetMapping("/boilerplate-page/{userId}")
    public String boilerplateForm(Model model, Authentication authentication, @PathVariable("userId") String userId) {
        model.addAttribute("userId", userId);

        if (userId.equals(authentication.getName())) {
            model.addAttribute("userId", userId);
            return "/boilerplate/boilerplate-page";
        }else {
            return "forbidden";
        }

    }

    @PostMapping("/boilerplate")
    public String boilerplateAdd(Model model, BoilerplateDTO boilerplateDTO) {
        boilerplateService.addBoilerplate(boilerplateDTO.toEntity());

        List<Boilerplate> boilerplateList = boilerplateService.findAllBoilerplate(boilerplateDTO.getAuthorUserId());
        model.addAttribute("boilerplateList", boilerplateList);

        return "/fragments/boilerplate/boilerplate-list";
    }

    @GetMapping("/boilerplates")
    public String boilerplateList(){

        return null;
    }
}
