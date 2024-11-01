package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String dashboard() {
        return "MainHomepage";
    }
    
    @GetMapping("/appointment")
    public String appointment() {
        return "appointment";
    }
    
    @GetMapping("/login")
    public String loginpage() {
        return "Login";
    }
    
    @GetMapping("/register")
    public String registerpage() {
        return "registration";
    }
    
    @GetMapping("/services")
    public String services() {
        return "services";
    }
}

