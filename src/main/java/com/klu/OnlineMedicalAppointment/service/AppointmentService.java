package com.klu.OnlineMedicalAppointment.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;

public interface AppointmentService {
	public Appointment makeAppointment(Appointment appointment);
	public List<Appointment> getPatientAppointments(Patient patient);
	public List<Appointment> getPatientAppointmentsByDoctor(Doctor doctor);
	public List<Appointment> getDoctorAppointmentsByDate(Doctor doctor, LocalDate date);
	public byte[] generateAppointmentReport(Long patientId) throws DocumentException, IOException ;
}
