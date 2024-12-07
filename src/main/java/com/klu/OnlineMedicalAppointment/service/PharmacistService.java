package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.Pharmacist;

public interface PharmacistService {
	Pharmacist savePharmacist(Pharmacist pharmacist);
	Pharmacist findPharmacistByEmail(String email);
	List<Pharmacist> getAllPharmacist();
	void deletPharmasist(Long id);
}
