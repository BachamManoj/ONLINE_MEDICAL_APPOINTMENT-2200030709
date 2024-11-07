package com.klu.OnlineMedicalAppointment.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.service.AppointmentService;
import com.klu.OnlineMedicalAppointment.service.DoctorService;
import com.klu.OnlineMedicalAppointment.service.PatientService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PatientController {
	
	@Autowired
	private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;

    
    
    @PostMapping(value = "/patientRegistration", consumes = {"multipart/form-data"})
    public ResponseEntity<String> registerPatient(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("dateOfBirth") LocalDate dateOfBirth,
            @RequestParam("gender") String gender,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address") String address,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {

        byte[] profileImageBytes = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileImageBytes = profileImage.getBytes();
            } catch (IOException e) {
                return ResponseEntity.badRequest().body("Failed to process image");
            }
        }

        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(gender);
        patient.setContactNumber(contactNumber);
        patient.setEmail(email);
        patient.setPassword(password);
        patient.setAddress(address);
        patient.setProfileImage(profileImageBytes); 
        
        patientService.patientRegestration(patient);

        return ResponseEntity.ok("Patient registered successfully.");
    }

    @GetMapping("/checkPatientExists")
    public boolean checkBYemailorContact(@RequestParam String email, @RequestParam String contactNumber) {
        return patientService.checkByemailorContact(email, contactNumber);
    }

    @PostMapping("/patientLogin")
    public ResponseEntity<Patient> login(@RequestBody Map<String, String> patientLoginData, HttpSession session) {
        String email = patientLoginData.get("email");
        String password = patientLoginData.get("password");
        Patient patient = patientService.checkPatientLogin(email, password);

        if (patient.getPassword().equals(password) && patient.getEmail().equals(email)) {
            session.setAttribute("patient", patient);
            session.setAttribute("patientEmail", patient.getEmail()); 
            return ResponseEntity.ok(patient); 
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
    }
    
    @GetMapping("/getPatientDetails")
    public ResponseEntity<Patient> getPatientDetails(HttpSession session) {
    	session.setMaxInactiveInterval(30*10);
        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }
        return ResponseEntity.ok(patient);
    }

    @PutMapping(value = "/updatePatientProfile", consumes = "multipart/form-data")
    public ResponseEntity<String> updatePatientProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("gender") String gender,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpSession session) {

        Patient patient = (Patient) session.getAttribute("patient");

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to update your profile.");
        }

        try {
            patient.setFirstName(firstName);
            patient.setLastName(lastName);
            patient.setDateOfBirth(LocalDate.parse(dateOfBirth));
            patient.setGender(gender);
            patient.setContactNumber(contactNumber);
            patient.setEmail(email);
            patient.setAddress(address);

            if (profileImage != null && !profileImage.isEmpty()) {
                byte[] profileImageBytes = profileImage.getBytes();
                patient.setProfileImage(profileImageBytes); 
            }

            patientService.updatePatientProfile(patient.getId(), patient);
            session.setAttribute("patient", patient); 

            return ResponseEntity.ok("Profile updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile image.");
        }
    }
    
    @GetMapping("/profile/{id}/image")
    public ResponseEntity<byte[]> getPatientImage(@PathVariable Long id)
    {
    	Patient patient = patientService.getImage(id);
    	byte[] image=patient.getProfileImage();  	
		return ResponseEntity.ok().body(image);
    }
    
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    
    @PostMapping("/getbyspecialty")
    public ResponseEntity<List<Doctor>> findBySpecialization(@RequestBody String specialization) {
        System.out.println("Received specialization: " + specialization); 
        List<Doctor> doctors = doctorService.getBySpecialization(specialization);
        
        if (doctors.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(doctors); 
    }

    
    @PostMapping("/makeAppointment")
    public ResponseEntity<String> makeDoctorAppointment(@RequestBody Appointment appointment) {
		Appointment app = appointmentService.makeAppointment(appointment);
			
		if(app!=null)
		{
			return ResponseEntity.ok("appointment done!!");	
		}
		return ResponseEntity.ok("sorry no appointment done!!");	
		
	}
    
}
