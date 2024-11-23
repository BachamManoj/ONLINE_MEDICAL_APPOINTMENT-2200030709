package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.EPrescription;
import com.klu.OnlineMedicalAppointment.model.Patient;

public interface EPrescriptionService {
	EPrescription saveEPrescription(EPrescription ePrescription);
	List<EPrescription> getAllEPrescriptions();
	EPrescription getEPrescriptionById(Long id);
	List<EPrescription> getEPrescriptionsByAppointment(Appointment appointment);
	EPrescription updateEPrescription(Long id, EPrescription updatedEPrescription);
	void deleteEPrescription(Long id);
	List<EPrescription> getEPrescriptionsByPatient(Patient patient);
	List<Long> getAcceptedEPrescription();
	List<EPrescription> findAppointment(Long appointmentId);
}
