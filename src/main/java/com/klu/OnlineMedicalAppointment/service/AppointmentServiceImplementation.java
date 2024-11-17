package com.klu.OnlineMedicalAppointment.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.repository.AppointmentRepository;
import com.klu.OnlineMedicalAppointment.repository.PatientRepository;

@Service
public class AppointmentServiceImplementation implements AppointmentService{

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	
	@Override
	public Appointment makeAppointment(Appointment appointment) {
		return appointmentRepository.save(appointment);
		
	}

	@Override
	public List<Appointment> getPatientAppointments(Patient patient) {
		return appointmentRepository.findByPatient(patient);
	}

	@Override
	public List<Appointment> getPatientAppointmentsByDoctor(Doctor doctor) {
		
		return appointmentRepository.findByDoctor(doctor);
	}
	
	@Override
	public List<Appointment> getDoctorAppointmentsByDate(Doctor doctor, LocalDate date) {
        return appointmentRepository.findByDoctorAndDate(doctor, date);
    }
	
	public byte[] generateAppointmentReport(Long patientId) throws DocumentException, IOException {
		@SuppressWarnings("deprecation")
		Patient patient=patientRepository.getById(patientId);
		List<Appointment> appointments=appointmentRepository.findByPatient(patient);
		
		Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        PdfWriter.getInstance(document, out);
        document.open();
        
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Medical Appointment Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Adding space
        
        // Table setup
        PdfPTable table = new PdfPTable(4); // Four columns: Appointment ID, Date, Doctor, Status
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 3, 3, 2});
        
        // Table headers
        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Appointment ID", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Date", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Doctor", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Status", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);
        
        // Fill table rows with appointment data
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Appointment appointment : appointments) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(appointment.getId().toString(), cellFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(appointment.getDate().format(dateFormatter), cellFont));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(appointment.getDoctor().getName(), cellFont));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(appointment.getIsCompleted() ? "Completed" : "Pending", cellFont));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        
        document.add(table);
        document.close();

        return out.toByteArray();
    }
}
