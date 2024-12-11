package com.klu.OnlineMedicalAppointment.controller;

import com.klu.OnlineMedicalAppointment.model.Report;
import com.klu.OnlineMedicalAppointment.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = {"https://sdp-java.vercel.app", "https://sdp2200030709.netlify.app"}, allowCredentials = "true")
public class ReportController {

	@Autowired
    private ReportService reportService;

   
    @PostMapping("/createReport")
    public ResponseEntity<Report> saveReport(@RequestBody Report report) {
        Report savedReport = reportService.saveReport(report);
        return ResponseEntity.ok(savedReport);
    }
    
    @GetMapping("/viewReport/{appointmentId}")
    public ResponseEntity<Report> viewReportsByAppointmentId(@PathVariable Long appointmentId) {
        Report reports = reportService.viewReport(appointmentId);
        return ResponseEntity.ok(reports);
    }
    
    @PutMapping("/editDescription/{appointmentId}")
    public ResponseEntity<Report> editDescription(@PathVariable Long appointmentId,@RequestBody Report updatedReport) {
        Report existingReport = reportService.viewReport(appointmentId);
        if (existingReport == null) {
            return ResponseEntity.notFound().build(); 
        }
        existingReport.setDescription(updatedReport.getDescription());
        Report updated = reportService.saveReport(existingReport);
        return ResponseEntity.ok(updated);
    }
    
}
