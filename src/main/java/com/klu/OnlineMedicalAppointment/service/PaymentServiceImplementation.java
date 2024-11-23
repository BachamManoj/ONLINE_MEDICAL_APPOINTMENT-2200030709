package com.klu.OnlineMedicalAppointment.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Appointment;
import com.klu.OnlineMedicalAppointment.model.Payment;
import com.klu.OnlineMedicalAppointment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.annotation.PostConstruct;

import org.json.JSONObject;

@Service
public class PaymentServiceImplementation implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    private RazorpayClient client;

    @PostConstruct
    public void initializeRazorpay() throws RazorpayException {
        client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }

    @Override
    public void createPayment(Payment payment) {
        paymentRepository.save(payment);	
    }

    public Order createRazorpayOrder(Double amount) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
        
        return client.orders.create(orderRequest);
    }

    @Override
    public List<Payment> getAppointmentDues(List<Long> appointmentIds) {
        return paymentRepository.findByAppointmentIdIn(appointmentIds);
    }
    
    @Override
    public List<Payment> getEprescriptionPaymentsByAppointmentIds(List<Long> appointmentIds) {
        return paymentRepository.findEprescriptionPaymentsByAppointmentIds(appointmentIds);
    }

	@Override
	public void deleteAppointment(Long paymentId) {
		paymentRepository.deleteById(paymentId);
		
	}

	@Override
	public Payment findByAppointmentId(Long appointmentId) {
		return paymentRepository.findByAppointmentId(appointmentId);
	}
	
	@Override
	public List<Payment> findByAppointmentIds(List<Appointment> appointmentIds) {
		return paymentRepository.findPaymentsByAppointmentIdsAndType(appointmentIds);
	}

	@Override
	public Payment getPrice(Long appointmentId) {
		return paymentRepository.findPrice(appointmentId);
	}
}
