package com.dus.back.boilerplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BoilerplateController {

    @GetMapping("/boilerplate-form/{userId}")
    public String boilerplateForm(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("userId", userId);
        return "/boilerplate/boilerplate-form";
    }

    @PostMapping("/boilerplate")
    @ResponseBody
    public void create() {

    }
}
