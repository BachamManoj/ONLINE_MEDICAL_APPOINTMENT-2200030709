package com.klu.OnlineMedicalAppointment.service;

import com.klu.OnlineMedicalAppointment.model.Pharmacist;

public interface PharmacistService {
	Pharmacist savePharmacist(Pharmacist pharmacist);
	Pharmacist findPharmacistByEmail(String email);
}
