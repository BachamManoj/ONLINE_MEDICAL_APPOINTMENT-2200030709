package com.klu.OnlineMedicalAppointment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.service.PatientService;

@RestController
@CrossOrigin("http://localhost:3000")
public class PatientController {
	
	@Autowired
	private PatientService patientService;
	
	@PostMapping("/patientRegistration")
	public String registration(@RequestBody Patient patient)
	{
		return patientService.patientRegestration(patient);
	}
	
	@GetMapping("/checkPatientExists")
	public boolean checkBYemailorContact(@RequestParam String email,@RequestParam String contactNumber)
	{
		return patientService.checkByemailorContact(email, contactNumber);
		
	}
	
	@PostMapping("/patientLogin")
	public boolean login(@RequestBody Map<String, String> patientLoginData) {
	    String email = patientLoginData.get("email");
	    String password = patientLoginData.get("password");
	    Patient patient = patientService.checkPatientLogin(email, password);
	    return patient != null;
	}

	
}
