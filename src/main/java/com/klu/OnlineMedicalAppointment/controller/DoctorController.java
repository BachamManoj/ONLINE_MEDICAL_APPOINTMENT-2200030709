package com.klu.OnlineMedicalAppointment.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.EPrescription;
import com.klu.OnlineMedicalAppointment.model.Payment;
import com.klu.OnlineMedicalAppointment.model.Report;
import com.klu.OnlineMedicalAppointment.service.AppointmentService;
import com.klu.OnlineMedicalAppointment.service.DoctorService;
import com.klu.OnlineMedicalAppointment.service.EPrescriptionService;
import com.klu.OnlineMedicalAppointment.service.PaymentService;
import com.klu.OnlineMedicalAppointment.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
@CrossOrigin(origins = {"https://sdp-java.vercel.app", "https://sdp2200030709.netlify.app"}, allowCredentials = "true")
public class DoctorController {
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
    private EPrescriptionService ePrescriptionService;
	
	@Autowired 
	private PaymentService paymentService;
	
	@PostMapping("/doctorlogin")
	public ResponseEntity<?> checkDoctorLogin(@RequestBody Doctor doctorCredentials,HttpSession session) {
	    String email = doctorCredentials.getEmail();
	    String password = doctorCredentials.getPassword();
	    Doctor doctor = doctorService.checkDoctorLogin(email, password);
	    
	    if (doctor != null && doctor.getPassword().equals(password) && doctor.getEmail().equals(email)) {
	        session.setAttribute("doctor", doctor);
	        session.setAttribute("doctorEmail", doctor.getEmail());
	    	return ResponseEntity.ok(doctor); 
	    }
	    
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
	}

	
	@GetMapping("/getDoctorDetails")
    public ResponseEntity<Doctor> getPatientDetails(HttpSession session) {
    	session.setMaxInactiveInterval(30*100);
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); 
        }
        return ResponseEntity.ok(doctor);
    }
	
	
	@GetMapping("/getPatientAppointments/{id}")
	public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable Long id) {
	    Doctor doctor = doctorService.finbById(id);
	    List<Appointment> appointments = appointmentService.getPatientAppointmentsByDoctor(doctor);
	    
	    if (appointments != null && !appointments.isEmpty()) {
	        return ResponseEntity.ok(appointments);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}
	
	@PutMapping("/updateReport/{appointmentId}")
    public ResponseEntity<Appointment> updateReportCompleted(@PathVariable Long appointmentId) {
        try 
        {
            Appointment updatedAppointment = appointmentService.updateReportCompleted(appointmentId);
            return ResponseEntity.ok(updatedAppointment);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).body(null);
        }
    }
	
	@PostMapping("/doctorlogout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }
	
	@GetMapping("/getMyPayments")
	public ResponseEntity<List<Payment>> viewMyPayments(HttpSession session)
	{
		Doctor doctor=(Doctor) session.getAttribute("doctor");
		List<Appointment> appointments=appointmentService.getPatientAppointmentsByDoctor(doctor);
		
		List<Payment> payments=paymentService.findByAppointmentIds(appointments);
		return ResponseEntity.ok(payments);
	}	
	
	@GetMapping("/viewMyFeedback")
	public ResponseEntity<List<Appointment>> viewMyfeedBack(HttpSession session)
	{
		Doctor doctor=(Doctor) session.getAttribute("doctor");
		List<Appointment> appointments=appointmentService.getPatientAppointmentsByDoctor(doctor);
		return ResponseEntity.ok(appointments);
	}
	
	@GetMapping("/Doctorprofile/{id}/image")
    public ResponseEntity<byte[]> getPatientImage(@PathVariable Long id)
    {
    	Doctor doctor=doctorService.getImage(id);
    	byte[] image=doctor.getProfileImage();  	
		return ResponseEntity.ok().body(image);
    }
	
	
	@PutMapping(value = "/updateDoctorProfile/{id}", consumes = "multipart/form-data")
	public ResponseEntity<?> updateDoctorProfile(@PathVariable Long id, 
	                                              @RequestParam("name") String name,
	                                              @RequestParam("specialization") String specialization,
	                                              @RequestParam("contactNumber") String contactNumber,
	                                              @RequestParam("email") String email,
	                                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
	                                              HttpSession session) 
	{
	    Doctor sessionDoctor = (Doctor) session.getAttribute("doctor");
	    if (sessionDoctor == null || !sessionDoctor.getId().equals(id))
	    {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
	    }

	    try {
	        Doctor doctor = doctorService.findDoctorById(id);
	        if (doctor == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
	        }

	        doctor.setName(name);
	        doctor.setSpecialization(specialization);
	        doctor.setContactNumber(contactNumber);
	        doctor.setEmail(email);

	        
	        if (profileImage != null && !profileImage.isEmpty()) {
	            doctor.setProfileImage(profileImage.getBytes()); 
	        }

	        doctorService.saveDoctor(doctor);
	        return ResponseEntity.ok("Doctor profile updated successfully.");
	    } 
	    catch (IOException e) 
	    {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the profile.");
	    }
	}
	
	@GetMapping("/getAllDoctorsList")
	public ResponseEntity<List<Doctor>> getAllDoctors()
	{
		return ResponseEntity.ok(doctorService.fetchAllDoctors());
	}
	
	@PutMapping("/OnlineConferance")
	public void provideUrl(@RequestParam String url,@RequestParam Long appointmentid)
	{
		Optional<Appointment> appointment=appointmentService.findAppointment(appointmentid);
		if(appointment.isPresent())
		{
			Appointment appointment2=appointment.get();
			appointment2.setAppointmentUrl(url);
			appointment2.setStatus("VIRTUAL APP ✔");
			appointment2.setIsCompleted(true);
			appointmentService.makeAppointment(appointment2);
		}
	}
	
	@GetMapping("/viewPatientMedicalReport/{patientId}/{appointmentId}")
    public ResponseEntity<byte[]> viewPatientMedicalReport(@PathVariable Long patientId, @PathVariable Long appointmentId, HttpSession session) 
	{
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        if (doctor == null) 
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try 
        {
            Report report = reportService.getReportByAppointmentIdAndPatientId(appointmentId, patientId);

            if (report == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Optional<Appointment> appointmentOpt = appointmentService.findAppointment(appointmentId);
            Appointment appointment = appointmentOpt.orElse(null);
            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<EPrescription> prescriptions = ePrescriptionService.getEPrescriptionsByAppointment(appointment);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Medical Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            Font patientInfoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Paragraph patientName = new Paragraph("Patient: " + report.getPatient().getFirstName() + " " + report.getPatient().getLastName(), patientInfoFont);
            Paragraph doctorName = new Paragraph("Doctor: " + report.getDoctor().getName() , patientInfoFont);
            Paragraph appointmentDate = new Paragraph("Appointment Date: " + report.getAppointment().getDate(), patientInfoFont);
            document.add(patientName);
            document.add(doctorName);
            document.add(appointmentDate);

            document.add(new Paragraph("\n"));

            Font descriptionFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            Paragraph description = new Paragraph("Report Description:", descriptionFont);
            description.setSpacingBefore(10);
            description.setSpacingAfter(10);
            document.add(description);

            Paragraph reportDescription = new Paragraph(report.getDescription(), descriptionFont);
            reportDescription.setIndentationLeft(20);  
            document.add(reportDescription);
            if (!prescriptions.isEmpty()) {
                document.add(new Paragraph("\nEPrescriptions:", descriptionFont));

                PdfPTable prescriptionTable = new PdfPTable(3); 
                prescriptionTable.setWidthPercentage(100); 
                Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
                PdfPCell header1 = new PdfPCell(new Phrase("Medicine Name", tableHeaderFont));
                header1.setBackgroundColor(BaseColor.BLUE);
                PdfPCell header2 = new PdfPCell(new Phrase("Description", tableHeaderFont));
                header2.setBackgroundColor(BaseColor.BLUE);
                PdfPCell header3 = new PdfPCell(new Phrase("Quantity", tableHeaderFont));
                header3.setBackgroundColor(BaseColor.BLUE);
                prescriptionTable.addCell(header1);
                prescriptionTable.addCell(header2);
                prescriptionTable.addCell(header3);
                for (EPrescription prescription : prescriptions) {
                    prescriptionTable.addCell(prescription.getMedicineName());
                    prescriptionTable.addCell(prescription.getDescription());
                    prescriptionTable.addCell(String.valueOf(prescription.getQuantity()));
                }

                document.add(prescriptionTable);
            }

            PdfPTable table = new PdfPTable(2); 
            table.setWidthPercentage(100); 

            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Key", tableHeaderFont));
            header1.setBackgroundColor(BaseColor.BLUE);
            PdfPCell header2 = new PdfPCell(new Phrase("Value", tableHeaderFont));
            header2.setBackgroundColor(BaseColor.BLUE);
            table.addCell(header1);
            table.addCell(header2);

            table.addCell("Patient ID");
            table.addCell(report.getPatient().getId().toString());
            table.addCell("Appointment ID");
            table.addCell(report.getAppointment().getId().toString());

            document.add(table);
            document.close();
            byte[] pdfContent = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "inline; filename=MedicalReport_" + patientId + "_" + appointmentId + ".pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}
