package com.klu.OnlineMedicalAppointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klu.OnlineMedicalAppointment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
	List<Payment> findByAppointmentIdIn(List<Long> appointmentIds);
}
