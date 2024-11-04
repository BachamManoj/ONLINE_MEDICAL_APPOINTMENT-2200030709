package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.repository.DoctorRepository;

@Service
public class DoctorServiceImplementation implements DoctorService{

	@Autowired
	DoctorRepository doctorRepository;
	
	@Override
	public Doctor checkDoctorLogin(String email, String password) {
		return doctorRepository.checkDoctorLogin(email, password);
	}

	@Override
	public List<Doctor> getBySpecialization(String specialization) {
		
		return doctorRepository.findBySpecialization(specialization);
	}

}
