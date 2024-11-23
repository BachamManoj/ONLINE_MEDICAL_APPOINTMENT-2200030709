package com.klu.OnlineMedicalAppointment.repository;

import com.klu.OnlineMedicalAppointment.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByAppointmentId(Long appointmentId);
    Report findByAppointmentIdAndPatientId(Long appointmentId, Long patientId);
}
