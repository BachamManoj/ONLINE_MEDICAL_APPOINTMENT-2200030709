package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;

public interface AppointmentService {
	public Appointment makeAppointment(Appointment appointment);
	public List<Appointment> getPatientAppointments(Patient patient);
	public List<Appointment> getPatientAppointmentsByDoctor(Doctor doctor);
}
