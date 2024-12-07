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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.klu.OnlineMedicalAppointment.model.EPrescription;
import com.klu.OnlineMedicalAppointment.model.Medicine;
import com.klu.OnlineMedicalAppointment.model.OrderMedicines;
import com.klu.OnlineMedicalAppointment.model.Payment;
import com.klu.OnlineMedicalAppointment.model.Pharmacist;
import com.klu.OnlineMedicalAppointment.service.EPrescriptionService;
import com.klu.OnlineMedicalAppointment.service.MedicineService;
import com.klu.OnlineMedicalAppointment.service.OrderMedicinesService;
import com.klu.OnlineMedicalAppointment.service.PaymentService;
import com.klu.OnlineMedicalAppointment.service.PharmacistService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "https://sdp2200030709.netlify.app/", allowCredentials = "true")
public class PharmacistController {
	@Autowired
    private PharmacistService pharmacistService;
	
	@Autowired
	private OrderMedicinesService orderMedicinesService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private EPrescriptionService ePrescriptionService;
	
	@Autowired
	private MedicineService medicineService;
	
	
	@PostMapping("/acceptOrder/{id}")
	public ResponseEntity<String> acceptOrderByPharmasist(@PathVariable Long id)
	{
		List<EPrescription> ePrescriptions=ePrescriptionService.findAppointment(id);
		for(EPrescription ePrescription:ePrescriptions)
		{
			int quantity=ePrescription.getQuantity();
			Long medId=ePrescription.getMedicine().getId();
			Optional<Medicine> medicine=medicineService.getMedicineById(medId);
			if(medicine.isPresent())
			{
				Medicine medicine2=medicine.get();
				int actuallQuantity=medicine2.getQuantity();
				medicine2.setQuantity(actuallQuantity-quantity);
				medicineService.saveMedicine(medicine2);
			}
		}
		orderMedicinesService.confirmOrder(id);
		return ResponseEntity.ok("Accepted and updated Medicines");
	}
	
	@GetMapping("/checkMedicinesAcceptOrder/{id}")
	public ResponseEntity<List<EPrescription>> checkMedicines(@PathVariable Long id)
	{
		List<EPrescription> ePrescriptions=ePrescriptionService.findAppointment(id);
		return ResponseEntity.ok(ePrescriptions);
	}
	

    @PostMapping("/registerPharmacist")
    public ResponseEntity<Pharmacist> registerPharmacist(@RequestBody Pharmacist pharmacist) {
        Pharmacist savedPharmacist = pharmacistService.savePharmacist(pharmacist);
        return ResponseEntity.ok(savedPharmacist);
    }

    @PostMapping("/Pharmacistlogin")
    public ResponseEntity<String> login(@RequestBody Pharmacist loginRequest,HttpSession session) {
        Pharmacist pharmacist = pharmacistService.findPharmacistByEmail(loginRequest.getEmail());
        if (pharmacist != null && pharmacist.getPassword().equals(loginRequest.getPassword())) {
        	session.setAttribute("pharmacist", pharmacist);
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
    
    @PostMapping("/Pharmacistlogout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }
    
    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderMedicines>> getAllOrders(HttpSession session)
    {
    	Pharmacist pharmacist = (Pharmacist) session.getAttribute("pharmacist");
    	if(pharmacist!=null)
    	{
    		List<OrderMedicines> orders = orderMedicinesService.getAllOrders();
            return ResponseEntity.ok(orders);
    	}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    
    @GetMapping("/getPriceOfOrder/{id}")
    public ResponseEntity<Double> getPrice(@PathVariable Long id, HttpSession session)
    {
    	Pharmacist pharmacist = (Pharmacist) session.getAttribute("pharmacist");
    	if(pharmacist!=null)
    	{
    		Payment payment= paymentService.getPrice(id);
    		return ResponseEntity.ok(payment.getAmount());
    	}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    
    @PostMapping("/updateStatusDispatched/{id}")
    public String updateStatusDispatched(@PathVariable Long id)
    {
    	OrderMedicines orderMedicines=orderMedicinesService.findOrderMedicines(id);
    	orderMedicines.setDispatched(true);
    	orderMedicinesService.createOrder(orderMedicines);
    	return "Order is Dispatched";
    }
    
    @PostMapping("/updateStatusinTransit/{id}")
    public String updateStatusinTransit(@PathVariable Long id)
    {
    	OrderMedicines orderMedicines=orderMedicinesService.findOrderMedicines(id);
    	orderMedicines.setInTransit(true);
    	orderMedicinesService.createOrder(orderMedicines);
    	return "Order is Dispatched";
    }
    
    @PostMapping("/updateStatusDelivered/{id}")
    public String updateStatusDelivered(@PathVariable Long id)
    {
    	OrderMedicines orderMedicines=orderMedicinesService.findOrderMedicines(id);
    	orderMedicines.setDelivered(true);
    	orderMedicinesService.createOrder(orderMedicines);
    	return "Order is Dispatched";
    }
}
