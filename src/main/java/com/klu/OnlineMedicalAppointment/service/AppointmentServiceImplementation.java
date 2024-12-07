package com.klu.OnlineMedicalAppointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Doctor;
import com.klu.OnlineMedicalAppointment.model.Patient;
import com.klu.OnlineMedicalAppointment.repository.AppointmentRepository;

@Service
public class AppointmentServiceImplementation implements AppointmentService{

	@Autowired
	private AppointmentRepository appointmentRepository;

	
	
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
	
	@Override
	public Optional<Appointment> findAppointment(Long id)
	{
		Optional<Appointment> appointment=appointmentRepository.findById(id);
		return appointment;
	}
	
	@Override
	public Appointment updateReportCompleted(Long appointmentId) {
	    Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
	    appointment.setReportCompleted(true);
	    return appointmentRepository.save(appointment);
	}
	
	@Override
	public void updateRatingIfCompleted(Long appointmentId, int rating, String ratingDescription) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (!appointment.getIsCompleted()) {
            throw new IllegalArgumentException("Appointment is not completed yet");
        }

        // Update the rating and description
        appointment.setRating(rating);
        appointment.setRatingDescription(ratingDescription);

        // Save the updated appointment
        appointmentRepository.save(appointment);
    }

	@Override
	public void deleteAppointment(Long appointmentId) {
		appointmentRepository.deleteById(appointmentId);
	}

	@Override
	public Long getTotalAppointmentCount() {
		return appointmentRepository.getTotalAppointmentCount();
	}
}
