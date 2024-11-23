package com.klu.OnlineMedicalAppointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;

public interface AppointmentService {
	public Appointment makeAppointment(Appointment appointment);
	public List<Appointment> getPatientAppointments(Patient patient);
	public List<Appointment> getPatientAppointmentsByDoctor(Doctor doctor);
	public List<Appointment> getDoctorAppointmentsByDate(Doctor doctor, LocalDate date);
	Optional<Appointment> findAppointment(Long id);
	Appointment updateReportCompleted(Long appointmentId); 
	public void updateRatingIfCompleted(Long appointmentId, int rating, String ratingDescription);
	public void deleteAppointment(Long appointmentId);
}
