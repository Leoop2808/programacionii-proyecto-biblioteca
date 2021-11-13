package com.webdashboard.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class HomeController {
    private static final String HOME_INDEX = "dashboard";
    private static final String VIEW_DASHBOARD = "dashboard";
    private static final String VIEW_USER = "user";
    private static final String VIEW_REPORTES = "table";

    @GetMapping("/")
    public String index(Model model){
        return HOME_INDEX;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return VIEW_DASHBOARD;
    }

    @GetMapping("/user")
    public String user(Model model) {
        return VIEW_USER;
    }

    @GetMapping("/table")
    public String table(Model model) {
        return VIEW_REPORTES;
    }
}
