package com.klu.OnlineMedicalAppointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klu.OnlineMedicalAppointment.model.Pharmacist;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {

    Pharmacist findByEmail(String email);
}
