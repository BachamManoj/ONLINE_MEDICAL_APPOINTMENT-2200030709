package com.klu.OnlineMedicalAppointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.repository.PatientRepository;

@Service
public class PatientServiceImplementation implements PatientService {

	@Autowired
	private PatientRepository patientRepository;
	
	@Override
	public String patientRegestration(Patient patient) {
		patientRepository.save(patient);
		return "Patient Regesterd sucessfully login to book appointment!!!...";
	}

	@Override
	public boolean checkByemailorContact(String email, String contactNumber) {
		
		return patientRepository.findByEmail(email).isPresent() || patientRepository.findByContactNumber(contactNumber).isPresent();
	}

	@Override
	public Patient checkPatientLogin(String email, String password) {
		
		return patientRepository.checkPatientLogin(email, password);
	}
	
}
