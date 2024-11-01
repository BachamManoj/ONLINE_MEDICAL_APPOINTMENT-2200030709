package com.klu.OnlineMedicalAppointment.service;

import com.klu.OnlineMedicalAppointment.model.Patient;

public interface PatientService {
	public String patientRegestration(Patient patient);
	public boolean checkByemailorContact(String email,String contactNumber);
	public Patient checkPatientLogin(String email,String password);
}
