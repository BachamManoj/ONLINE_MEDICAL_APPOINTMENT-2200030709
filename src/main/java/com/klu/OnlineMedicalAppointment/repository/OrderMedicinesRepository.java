package com.klu.OnlineMedicalAppointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klu.OnlineMedicalAppointment.model.OrderMedicines;

@Repository
public interface OrderMedicinesRepository extends JpaRepository<OrderMedicines, Long> {
	OrderMedicines findByAppointmentId(Long id);
}
