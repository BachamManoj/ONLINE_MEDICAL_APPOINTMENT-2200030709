package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Payment;


public interface PaymentService {
	public void createPayment(Payment payment);
	public List<Payment> getAppointmentDues(List<Long> appointmentids);
	List<Payment> getEprescriptionPaymentsByAppointmentIds(List<Long> appointmentIds);
	public void deleteAppointment(Long paymentId);
	Payment findByAppointmentId(Long appointmentId);
	List<Payment> findByAppointmentIds(List<Appointment> appointmentIds);
	Payment getPrice(Long appointmentId);
}
