package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.service.DoctorService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class DoctorController {
	
	@Autowired
	private DoctorService doctorService;
	
	@PostMapping("/doctorlogin")
	public ResponseEntity<Doctor> checkDoctorLogin(@RequestBody Doctor doctorCredindials)
	{
		String email=doctorCredindials.getEmail();
		String password=doctorCredindials.getPassword();
		Doctor doctor = doctorService.checkDoctorLogin(email, password);
		
		if(doctor.getPassword().equals(password) && doctor.getEmail().equals(email))
		{
			
			return ResponseEntity.ok(doctor);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	
	
}
