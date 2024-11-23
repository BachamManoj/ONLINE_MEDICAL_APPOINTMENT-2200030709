package com.klu.OnlineMedicalAppointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.EPrescription;
import com.klu.OnlineMedicalAppointment.model.Patient;
@Repository
public interface EPrescriptionRepository extends JpaRepository<EPrescription, Long> {

	@Query("SELECT DISTINCT e.appointment.id FROM EPrescription e WHERE e.accept = true")
	List<Long> findAcceptedUniqueAppointmentIds();
	List<EPrescription> findByAppointment(Appointment appointmentId);
	List<EPrescription> findByAppointmentId(Long appointmentId);
	List<EPrescription> findByPatient(Patient patient);
}
