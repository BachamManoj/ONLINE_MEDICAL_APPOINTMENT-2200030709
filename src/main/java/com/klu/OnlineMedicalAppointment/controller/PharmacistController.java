package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klu.OnlineMedicalAppointment.model.Pharmacist;
import com.klu.OnlineMedicalAppointment.service.PharmacistService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PharmacistController {
	@Autowired
    private PharmacistService pharmacistService;

    @PostMapping("/registerPharmacist")
    public ResponseEntity<Pharmacist> registerPharmacist(@RequestBody Pharmacist pharmacist) {
        Pharmacist savedPharmacist = pharmacistService.savePharmacist(pharmacist);
        return ResponseEntity.ok(savedPharmacist);
    }

    @PostMapping("/Pharmacistlogin")
    public ResponseEntity<String> login(@RequestBody Pharmacist loginRequest) {
        Pharmacist pharmacist = pharmacistService.findPharmacistByEmail(loginRequest.getEmail());
        if (pharmacist != null && pharmacist.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
