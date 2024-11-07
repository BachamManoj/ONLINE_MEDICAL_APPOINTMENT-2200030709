package com.klu.OnlineMedicalAppointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.repository.AppointmentRepository;

@Service
public class AppointmentServiceImplementation implements AppointmentService{

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Override
	public Appointment makeAppointment(Appointment appointment) {
		return appointmentRepository.save(appointment);
		
	}
	
}
