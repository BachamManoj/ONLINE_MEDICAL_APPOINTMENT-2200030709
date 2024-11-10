package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String dashboard() {
        return "MainHomepage";
    }
    
    @GetMapping("/appointment")
    public String appointmentpage() {
        return "appointment";
    }
    
    @GetMapping("/login")
    public String loginpage() {
        return "Login";
    }
    
    @GetMapping("/register")
    public String registerpagepage() {
        return "registration";
    }
    
    @GetMapping("/services")
    public String servicespage() {
        return "services";
    }
    
    @GetMapping("/about")
    public String aboutpage() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contactpage() {
        return "contact";
    }
    
    
    @GetMapping("/doctorLogin")
    public String doctorloginpage() {
        return "Doctorlogin";
    }
}

