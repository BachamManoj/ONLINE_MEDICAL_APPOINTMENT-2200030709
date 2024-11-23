package com.klu.OnlineMedicalAppointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
	List<Payment> findByAppointmentIdIn(List<Long> appointmentIds);
	
	@Query("SELECT p FROM Payment p WHERE p.appointment.id IN :appointmentIds AND p.type = 1")
	List<Payment> findEprescriptionPaymentsByAppointmentIds(@Param("appointmentIds") List<Long> appointmentIds);
	
	Payment findByAppointmentId(Long appointmentId);
	
	@Query("SELECT p FROM Payment p WHERE p.appointment IN :appointmentIds AND p.type = 0")
	List<Payment> findPaymentsByAppointmentIdsAndType(@Param("appointmentIds") List<Appointment> appointmentIds);

	
	@Query("SELECT p FROM Payment p WHERE p.appointment.id = :appointmentId AND p.type = 1")
	Payment findPrice(@Param("appointmentId") Long appointmentId);
	
}
