package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.repository.AppointmentRepository;

@Service
public class AppointmentServiceImplementation implements AppointmentService{

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Override
	public Appointment makeAppointment(Appointment appointment) {
		return appointmentRepository.save(appointment);
		
	}

	@Override
	public List<Appointment> getPatientAppointments(Patient patient) {
		return appointmentRepository.findByPatient(patient);
	}

	@Override
	public List<Appointment> getPatientAppointmentsByDoctor(Doctor doctor) {
		
		return appointmentRepository.findByDoctor(doctor);
	}
	
}
