package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.Doctor;

public interface DoctorService {
	public Doctor checkDoctorLogin(String email,String password);
	public List<Doctor> getBySpecialization(String specialization);
}
