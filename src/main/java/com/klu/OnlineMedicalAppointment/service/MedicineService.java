package com.klu.OnlineMedicalAppointment.service;

import java.util.List;
import java.util.Optional;

import com.klu.OnlineMedicalAppointment.model.Medicine;

public interface MedicineService {
	Medicine saveMedicine(Medicine medicine);
	List<Medicine> getAllMedicines();
	Optional<Medicine> getMedicineById(Long id);
	void deleteMedicine(Long id);
}
