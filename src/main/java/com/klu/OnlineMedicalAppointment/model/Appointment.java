package com.klu.OnlineMedicalAppointment.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;


    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Kolkata")
    @NotNull(message = "Date is required")
    private LocalDate date;

    
    @NotNull(message = "Time slot is required")
    private LocalTime timeSlot; 

    @Column(nullable = false)
    private Boolean isCompleted = false;
    
    @Column(nullable = false)
    private Boolean reportCompleted = false;
    
    private int rating;
    
    private String ratingDescription;
    
    private String appointmentUrl;
    
    @JsonIgnore
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    
    public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public Boolean getReportCompleted() {
		return reportCompleted;
	}

	public void setReportCompleted(Boolean reportCompleted) {
		this.reportCompleted = reportCompleted;
	}

	private String status;
   
    public Appointment() {}

    public Appointment(Patient patient, Doctor doctor, LocalDate date, LocalTime timeSlot,String status) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.timeSlot = timeSlot;
        this.isCompleted = false;
        this.reportCompleted=false;
        this.setStatus(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(LocalTime timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(Boolean isCompleted)
    {
        this.isCompleted = isCompleted;
        if (isCompleted && this.payment != null) 
        {
            this.payment.setIsPaid(true);
        }
    }
    
    @JsonIgnore
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Payment payment;

    public Payment getPayment() 
    { 
    	return payment; 
    }

    public void setPayment(Payment payment) 
    {
    	this.payment = payment; 
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getRatingDescription() {
		return ratingDescription;
	}

	public void setRatingDescription(String ratingDescription) {
		this.ratingDescription = ratingDescription;
	}

	public String getAppointmentUrl() {
		return appointmentUrl;
	}

	public void setAppointmentUrl(String appointmentUrl) {
		this.appointmentUrl = appointmentUrl;
	}
    
}
