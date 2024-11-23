package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.EPrescription;
import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.repository.EPrescriptionRepository;

@Service
public class EPrescriptionServiceImplementation implements EPrescriptionService{
	@Autowired
    private EPrescriptionRepository ePrescriptionRepository;

    @Override
    public EPrescription saveEPrescription(EPrescription ePrescription) {
        return ePrescriptionRepository.save(ePrescription);
    }

    @Override
    public List<EPrescription> getAllEPrescriptions() {
        return ePrescriptionRepository.findAll();
    }

    @Override
    public EPrescription getEPrescriptionById(Long id) {
        return ePrescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EPrescription not found with ID: " + id));
    }

	@Override
	public List<EPrescription> getEPrescriptionsByAppointment(Appointment appointment) {
		return ePrescriptionRepository.findByAppointment(appointment);
	}
    
	@Override
	public EPrescription updateEPrescription(Long id, EPrescription updatedEPrescription) {
		EPrescription eprescription=getEPrescriptionById(id);
		eprescription.setMedicine(updatedEPrescription.getMedicine());
        eprescription.setMedicineName(updatedEPrescription.getMedicineName());
        eprescription.setDescription(updatedEPrescription.getDescription());
        eprescription.setQuantity(updatedEPrescription.getQuantity());
        return ePrescriptionRepository.save(eprescription);
    }
	
	@Override
	public void deleteEPrescription(Long id) {
        ePrescriptionRepository.deleteById(id);
    }

	@Override
	public List<EPrescription> getEPrescriptionsByPatient(Patient patient) {
		
		return ePrescriptionRepository.findByPatient(patient);
	}

	@Override
	public List<Long> getAcceptedEPrescription() {
		return ePrescriptionRepository.findAcceptedUniqueAppointmentIds();
	}

	@Override
	public List<EPrescription> findAppointment(Long appointmentId) {
		return ePrescriptionRepository.findByAppointmentId(appointmentId);
	}

}
