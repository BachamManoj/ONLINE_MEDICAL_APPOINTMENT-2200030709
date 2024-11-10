package com.klu.OnlineMedicalAppointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.service.AppointmentService;
import com.klu.OnlineMedicalAppointment.service.DoctorService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class DoctorController {
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@PostMapping("/doctorlogin")
	public ResponseEntity<?> checkDoctorLogin(@RequestBody Doctor doctorCredentials,HttpSession session) {
	    String email = doctorCredentials.getEmail();
	    String password = doctorCredentials.getPassword();
	    Doctor doctor = doctorService.checkDoctorLogin(email, password);
	    
	    if (doctor != null && doctor.getPassword().equals(password) && doctor.getEmail().equals(email)) {
	        session.setAttribute("doctor", doctor);
	        session.setAttribute("doctorEmail", doctor.getEmail());
	    	return ResponseEntity.ok(doctor); 
	    }
	    
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
	}

	
	@GetMapping("/getDoctorDetails")
    public ResponseEntity<Doctor> getPatientDetails(HttpSession session) {
    	session.setMaxInactiveInterval(30*10);
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }
        return ResponseEntity.ok(doctor);
    }
	
	
	@GetMapping("/getPatientAppointments/{id}")
	public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long id) {
	    Doctor doctor = doctorService.finbById(id);
	    List<Appointment> appointments = appointmentService.getPatientAppointmentsByDoctor(doctor);
	    
	    if (appointments != null && !appointments.isEmpty()) {
	        return ResponseEntity.ok(appointments);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}

	
	@PostMapping("/doctorlogout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }
	
}
