package com.klu.OnlineMedicalAppointment.service;

import com.klu.OnlineMedicalAppointment.model.Report;


public interface ReportService {
    Report saveReport(Report report);
	Report viewReport(Long appointmentId);
	Report getReportByAppointmentIdAndPatientId(Long appointmentId, Long patientId);
}
