package com.klu.OnlineMedicalAppointment.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.EPrescription;
import com.klu.OnlineMedicalAppointment.model.OrderMedicines;
import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.model.Payment;
import com.klu.OnlineMedicalAppointment.model.Report;
import com.klu.OnlineMedicalAppointment.service.AppointmentService;
import com.klu.OnlineMedicalAppointment.service.DoctorService;
import com.klu.OnlineMedicalAppointment.service.EPrescriptionService;
import com.klu.OnlineMedicalAppointment.service.OrderMedicinesService;
import com.klu.OnlineMedicalAppointment.service.PatientService;
import com.klu.OnlineMedicalAppointment.service.PaymentService;
import com.klu.OnlineMedicalAppointment.service.ReportService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = {"https://sdp-java.vercel.app", "https://sdp2200030709.netlify.app"}, allowCredentials = "true")
public class PatientController {
	
	@Autowired
	private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PaymentService paymentService;
    
    @Autowired 
    private ReportService reportService;
    
    @Autowired
    private EPrescriptionService ePrescriptionService;
    
    @Autowired
    private OrderMedicinesService orderMedicinesService;

   
    
    @PostMapping(value = "/patientRegistration", consumes = {"multipart/form-data"})
    public ResponseEntity<String> registerPatient(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("dateOfBirth") LocalDate dateOfBirth,
            @RequestParam("gender") String gender,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("address") String address,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
    	
    	if (patientService.checkByemailorContact(email,contactNumber)) {
            return ResponseEntity.badRequest().body("Email or Contact already exists.");
        }
        byte[] profileImageBytes = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileImageBytes = profileImage.getBytes();
            } catch (IOException e) {
                return ResponseEntity.badRequest().body("Failed to process image");
            }
        }

        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(gender);
        patient.setContactNumber(contactNumber);
        patient.setEmail(email);
        patient.setPassword(password);
        patient.setAddress(address);
        patient.setProfileImage(profileImageBytes); 
        
        patientService.patientRegestration(patient);

        return ResponseEntity.ok("Patient registered successfully.");
    }

    @GetMapping("/checkPatientExists")
    public boolean checkBYemailorContact(@RequestParam String email, @RequestParam String contactNumber) {
        return patientService.checkByemailorContact(email, contactNumber);
    }

    @GetMapping("/patientLoginSession")
    public ResponseEntity<Patient> checkSession(HttpSession session) {
        Patient patientSession = (Patient) session.getAttribute("patient");
        if (patientSession != null) {
        	session.setMaxInactiveInterval(30*10);
            return ResponseEntity.ok(patientSession);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    
    @PostMapping("/patientLogin")
    public ResponseEntity<Patient> login(@RequestBody Map<String, String> patientLoginData, HttpSession session, HttpServletResponse response) {

        // Extract email and password from the request body
        String email = patientLoginData.get("email");
        String password = patientLoginData.get("password");

        // Validate login credentials
        Patient patient = patientService.checkPatientLogin(email, password);

        // Check if patient is found and credentials match
        if (patient != null && patient.getPassword().equals(password) && patient.getEmail().equals(email)) {
            
            // Store patient object in session
            session.setAttribute("patient", patient);
            session.setAttribute("patientEmail", patient.getEmail());

            // Set session cookie for cross-site requests (if needed)
            Cookie cookie = new Cookie("patientSessionId", session.getId());
            cookie.setHttpOnly(true); // Prevent JavaScript access to cookie
            cookie.setSecure(true); // Only send cookie over HTTPS
            cookie.setPath("/"); // Make the cookie accessible throughout the app
            cookie.setDomain("https://sdp-java.vercel.app/patientlogin"); // Set the domain to your frontend domain (replace with actual)
            cookie.setMaxAge(60 * 60); // Set cookie expiration time (1 hour)

            // Add cookie to the response
            response.addCookie(cookie);

            return ResponseEntity.ok(patient); // Respond with patient data if login is successful
        }

        // Respond with unauthorized if login fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    
    @GetMapping("/getPatientDetails")
    public ResponseEntity<Patient> getPatientDetails(HttpServletRequest request, HttpSession session) {
        // Set session timeout to 10 minutes
        session.setMaxInactiveInterval(30 * 10);

        // Check for session ID in the cookies (to support cookie-based session management)
        Cookie[] cookies = request.getCookies();
        String sessionIdFromCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("patientSessionId".equals(cookie.getName())) {
                    sessionIdFromCookie = cookie.getValue();
                    break;
                }
            }
        }

        // If no session ID in the cookie, return unauthorized response
        if (sessionIdFromCookie == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Validate the session ID from the cookie (this would require storing the session ID in a more persistent store if needed)
        // In a typical scenario, you would have a session management service to verify the session ID validity
        if (!session.getId().equals(sessionIdFromCookie)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Retrieve patient data from the session
        Patient patient = (Patient) session.getAttribute("patient");

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Return patient details if session is valid
        return ResponseEntity.ok(patient);
    }


    @PutMapping(value = "/updatePatientProfile", consumes = "multipart/form-data")
    public ResponseEntity<String> updatePatientProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("gender") String gender,
            @RequestParam("contactNumber") String contactNumber,
            @RequestParam("email") String email,
            @RequestParam("address") String address,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpSession session) {

        Patient patient = (Patient) session.getAttribute("patient");

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in to update your profile.");
        }

        try {
            patient.setFirstName(firstName);
            patient.setLastName(lastName);
            patient.setDateOfBirth(LocalDate.parse(dateOfBirth));
            patient.setGender(gender);
            patient.setContactNumber(contactNumber);
            patient.setEmail(email);
            patient.setAddress(address);

            if (profileImage != null && !profileImage.isEmpty()) {
                byte[] profileImageBytes = profileImage.getBytes();
                patient.setProfileImage(profileImageBytes); 
            }

            patientService.updatePatientProfile(patient.getId(), patient);
            session.setAttribute("patient", patient); 

            return ResponseEntity.ok("Profile updated successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile image.");
        }
    }
    
    @GetMapping("/profile/{id}/image")
    public ResponseEntity<byte[]> getPatientImage(@PathVariable Long id)
    {
    	Patient patient = patientService.getImage(id);
    	byte[] image=patient.getProfileImage();  	
		return ResponseEntity.ok().body(image);
    }
    
    @GetMapping("/getappointments/{id}")
    public ResponseEntity<List<Appointment>> getPatientAppointment(@PathVariable Long id)
    {
    	Patient patient=patientService.getImage(id);
    	List<Appointment> appointments=appointmentService.getPatientAppointments(patient);
    	if(appointments!=null)
    	{
    		return ResponseEntity.ok(appointments);
    	}
		return null;
    	
    }
    
    @GetMapping("/getOrdersbyPatient")
    public ResponseEntity<List<OrderMedicines>> getAllOrders(HttpSession session) 
    {
        Patient patient = (Patient) session.getAttribute("patient");

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
       
        List<Appointment> appointments = appointmentService.getPatientAppointments(patient);

        List<OrderMedicines> orderMedicines = new ArrayList<>();
        for (Appointment appointment : appointments) {
            OrderMedicines order = orderMedicinesService.findOrderMedicinesByAppointment(appointment.getId());
            if (order != null) {
                orderMedicines.add(order);
            }
        }
        return ResponseEntity.ok(orderMedicines);
    }

    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    
    @PostMapping("/getbyspecialty")
    public ResponseEntity<List<Doctor>> findBySpecialization(@RequestBody String specialization) {
        System.out.println("Received specialization: " + specialization); 
        List<Doctor> doctors = doctorService.getBySpecialization(specialization);
        System.out.println(doctors);
        if (doctors.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(doctors); 
    }

    
    @PostMapping("/makeAppointment")
    public ResponseEntity<String> makeDoctorAppointment(@RequestBody Appointment appointment) {
		Appointment app = appointmentService.makeAppointment(appointment);
		if(app!=null)
		{
			return ResponseEntity.ok("appointment done!!");	
		}
		return ResponseEntity.ok("sorry no appointment done!!");	
		
	}
    
    @GetMapping("/getPatientDataById/{id}")
    public ResponseEntity<Patient> getPatientData(@PathVariable Long id)
    {
    	Patient patient=patientService.getPatinetData(id);
    	if(patient!=null)
    	{
    		return ResponseEntity.ok(patient);
    	}
		return null;
    	
    }
    
    @GetMapping("/getPatientBillings")
    public ResponseEntity<List<Payment>> appointmentBilling(HttpSession session)
    {
    	Patient patient = (Patient) session.getAttribute("patient");
    	List<Appointment> appointments=appointmentService.getPatientAppointments(patient);
    	
    	List<Long> incompleteAppointmentIds = appointments.stream().map(Appointment::getId).collect(Collectors.toList());
    	
    	List<Payment> payments = paymentService.getAppointmentDues(incompleteAppointmentIds);
    	
    	List<Payment> filteredPayments = payments.stream()
    		    .filter(payment -> payment.getType() == 0)
    		    .collect(Collectors.toList());

		return ResponseEntity.ok(filteredPayments);
	}
    
    
    @GetMapping("/getPatientBillingsEprescription")
    public ResponseEntity<List<Payment>> getEprescriptionBilling(HttpSession session) {
        Patient patient = (Patient) session.getAttribute("patient");
        if (patient == null) {
            return ResponseEntity.status(401).build(); 
        }      
        List<Appointment> appointments = appointmentService.getPatientAppointments(patient);
        List<Long> incompleteAppointmentIds = appointments.stream().map(Appointment::getId).collect(Collectors.toList());

        if (incompleteAppointmentIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<Payment> ePrescriptionPayments = paymentService.getEprescriptionPaymentsByAppointmentIds(incompleteAppointmentIds);

        return ResponseEntity.ok(ePrescriptionPayments);
    }
    
    
    
    @GetMapping("/getDoctorFreeSlot/{id}/{date}")
    public ResponseEntity<List<LocalTime>> getDoctorFreeSlot(@PathVariable Long id,@PathVariable LocalDate date) {
        try {
            
            Doctor doctor = doctorService.finbById(id);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.emptyList()); 
            }
            List<Appointment> appointments = appointmentService.getDoctorAppointmentsByDate(doctor, date);
            List<LocalTime> bookedSlots = appointments.stream().map(Appointment::getTimeSlot).collect(Collectors.toList());
            return ResponseEntity.ok(bookedSlots);
        } 
        catch (Exception e) 
        {
          
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
    
    
	@GetMapping("/getAlreadybookedDates/{id}")
    public ResponseEntity<List<LocalDate>>getAlreadybookedDates(@PathVariable Long id)
    {
    	Patient patient=patientService.getImage(id);
    	List<Appointment> appointments=appointmentService.getPatientAppointments(patient);
    	List<LocalDate> bookedDates = new ArrayList<>();
    	for(Appointment appointment:appointments)
    	{
    		bookedDates.add(appointment.getDate());
    	}
    	
		return ResponseEntity.ok(bookedDates);
    	
    }
	
	
	@PutMapping("/ratebyPatient/{appointmentId}")
    public ResponseEntity<String> updateRating(@PathVariable Long appointmentId,@RequestBody Appointment ratingRequest) 
	{
        try 
        {
            if (ratingRequest.getRating() < 1 || ratingRequest.getRating() > 5) 
            {
                return ResponseEntity.status(400).body("Rating must be between 1 and 5");
            }

            appointmentService.updateRatingIfCompleted(appointmentId,ratingRequest.getRating(),ratingRequest.getRatingDescription());

            return ResponseEntity.ok("Rating updated successfully");
        }
        catch (IllegalArgumentException e) 
        {
            return ResponseEntity.status(400).body("Appointment not completed, unable to update rating");
        }
        catch (Exception e) 
        {
            return ResponseEntity.status(500).body("Failed to update rating");
        }
    }
	
	
	@DeleteMapping("/cancleApointment/{appointmentId}")
	public ResponseEntity<String> cancelAppointmentbyPatient(@PathVariable Long appointmentId)
	{
		Payment payment=paymentService.findByAppointmentId(appointmentId);
		if(payment != null)
			paymentService.deleteAppointment(payment.getId());
		appointmentService.deleteAppointment(appointmentId);
		return ResponseEntity.ok("Appointment cancled succesfully");	
	}
	
	 @PostMapping("/create")
	 public ResponseEntity<String> createOrder(@RequestBody OrderMedicines order) 
	 {
	  	order.setOrderDate(LocalDateTime.now());
	    orderMedicinesService.createOrder(order);
	    return ResponseEntity.ok("Order created successfully!");
	 }
	   
	 @PostMapping("/confirmOrder/{id}")
	 public ResponseEntity<String> confirmOrder(@PathVariable Long id)
	 {
	  	orderMedicinesService.confirmPayment(id);
	 	return ResponseEntity.ok("Order confirmed successfully!");	
	 }	
	
	
	@GetMapping("/viewPatientMedicalReportbyPatient/{appointmentId}/{doctorId}")
	public ResponseEntity<byte[]> viewPatientMedicalReport(@PathVariable Long appointmentId,@PathVariable Long doctorId, HttpSession session)
	{
	    Patient patient = (Patient) session.getAttribute("patient");
	    if (patient == null)
	    {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  
	    }

	    try 
	    {
	        Optional<Appointment> appointmentOpt = appointmentService.findAppointment(appointmentId);
	        if (!appointmentOpt.isPresent())
	        {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  
	        }
	        Appointment appointment = appointmentOpt.get();

	        if (!appointment.getDoctor().getId().equals(doctorId)) 
	        {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);  
	        }

	        if (!appointment.getPatient().getId().equals(patient.getId())) 
	        {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);  
	        }

	        Report report = reportService.getReportByAppointmentIdAndPatientId(appointmentId, patient.getId());
	        if (report == null)
	        {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Report not found
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
	        Paragraph doctorName = new Paragraph("Doctor: " + report.getDoctor().getName(), patientInfoFont);
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

	        if (!prescriptions.isEmpty())
	        {
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

	        PdfPTable table = new PdfPTable(2); // Two columns
	        table.setWidthPercentage(100); // Table width in percentage

	        PdfPCell header1 = new PdfPCell(new Phrase("Key"));
	        header1.setBackgroundColor(BaseColor.BLUE);
	        PdfPCell header2 = new PdfPCell(new Phrase("Value"));
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
	        headers.add("Content-Disposition", "inline; filename=MedicalReport_" + patient.getId() + "_" + appointmentId + ".pdf");

	        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);

	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  
	    }
	}


	
}
