package com.klu.OnlineMedicalAppointment.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
	List<Appointment> findByPatient(Patient patient); 
	List<Appointment> findByDoctor(Doctor doctor);
	List<Appointment> findByDoctorAndDate(Doctor doctor, LocalDate date);
}
