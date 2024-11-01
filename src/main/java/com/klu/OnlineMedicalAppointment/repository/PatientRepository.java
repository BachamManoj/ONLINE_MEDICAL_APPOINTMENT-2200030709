package com.klu.OnlineMedicalAppointment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klu.OnlineMedicalAppointment.model.Patient;
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	
	@Query("SELECT p FROM Patient p WHERE p.email = ?1 AND p.password = ?2")
	Patient checkPatientLogin(String email, String password);

	
	
	Optional<Patient> findByEmail(String email);
	Optional<Patient> findByContactNumber(String contactNumber);
}
