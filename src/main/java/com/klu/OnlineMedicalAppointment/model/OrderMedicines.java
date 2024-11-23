package com.klu.OnlineMedicalAppointment.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OrderMedicines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false)
    private Boolean delivered = false;

    @Column(nullable = false)
    private Boolean inTransit = false;

    @Column(nullable = false)
    private Boolean dispatched = false;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private LocalDateTime orderDate;
    
    @Column(nullable = false)
    private Boolean isPaid = false;

    @Column(nullable = false)
    private Boolean accept = false;

    
    public Boolean getAccept() {
		return accept;
	}

	public void setAccept(Boolean accept) {
		this.accept = accept;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public OrderMedicines() {}

    public OrderMedicines(Appointment appointment, String address) {
        this.appointment = appointment;
        this.address = address;
        this.orderDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getInTransit() {
        return inTransit;
    }

    public void setInTransit(Boolean inTransit) {
        this.inTransit = inTransit;
    }

    public Boolean getDispatched() {
        return dispatched;
    }

    public void setDispatched(Boolean dispatched) {
        this.dispatched = dispatched;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
