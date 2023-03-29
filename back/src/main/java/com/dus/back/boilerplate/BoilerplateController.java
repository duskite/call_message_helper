package com.dus.back.boilerplate;

import com.dus.back.domain.Boilerplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class BoilerplateController {

    private final BoilerplateService boilerplateService;

    public BoilerplateController(BoilerplateService boilerplateService) {
        this.boilerplateService = boilerplateService;
    }

    @GetMapping("/boilerplate-page/{userId}")
    public String boilerplatePage(Model model, Authentication authentication, @PathVariable("userId") String userId) {

        if(!userId.equals(authentication.getName())){
            return "forbidden";
        }

        List<Boilerplate> boilerplateList = boilerplateService.findAllBoilerplate(userId);

        model.addAttribute("boilerplateList", boilerplateList);
        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);

        return "/boilerplate/boilerplate-page";
    }

    @PostMapping("/boilerplate")
    public String boilerplateAdd(Model model, BoilerplateDTO boilerplateDTO, Authentication authentication) {

        boilerplateService.addBoilerplate(boilerplateDTO.toEntity());

        List<Boilerplate> boilerplateList = boilerplateService.findAllBoilerplate(authentication.getName());

        model.addAttribute("boilerplateList", boilerplateList);
        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);

        return "/fragments/boilerplate/boilerplate-list";
    }

    @DeleteMapping("/boilerplate")
    public String boilerplateDelete(Model model, BoilerplateDTO boilerplateDTO, Authentication authentication) {

        boilerplateService.removeBoilerplate(boilerplateDTO.toEntity());

        List<Boilerplate> boilerplateList = boilerplateService.findAllBoilerplate(authentication.getName());

        model.addAttribute("boilerplateList", boilerplateList);
        model.addAttribute("userId", authentication.getName());
        model.addAttribute("isEditablePage", true);

        return "/fragments/boilerplate/boilerplate-list";
    }
}
