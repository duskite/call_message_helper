package com.dus.back.team;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping("/team-page/admin/{adminUserId}")
    public String teamPage(Model model, Authentication authentication) {

        model.addAttribute("adminUserId", authentication.getName());


        return "/team/team-page";
    }
}
