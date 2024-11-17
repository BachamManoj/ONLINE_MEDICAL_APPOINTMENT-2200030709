package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.Payment;


public interface PaymentService {
	public void createPayment(Payment payment);
	public List<Payment> getAppointmentDues(List<Long> appointmentids);
	}
