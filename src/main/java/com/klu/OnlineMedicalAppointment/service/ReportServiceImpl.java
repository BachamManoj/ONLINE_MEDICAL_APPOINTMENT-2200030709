package com.klu.OnlineMedicalAppointment.service;

import com.klu.OnlineMedicalAppointment.model.Report;
import com.klu.OnlineMedicalAppointment.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
    private ReportRepository reportRepository;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report viewReport(Long appointmentId) {
        return reportRepository.findByAppointmentId(appointmentId);
    }
    public Report getReportByAppointmentIdAndPatientId(Long appointmentId, Long patientId) {
        return reportRepository.findByAppointmentIdAndPatientId(appointmentId, patientId); 
    }
}

