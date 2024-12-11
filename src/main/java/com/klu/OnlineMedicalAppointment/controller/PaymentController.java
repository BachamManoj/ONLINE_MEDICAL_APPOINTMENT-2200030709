package com.klu.OnlineMedicalAppointment.controller;

import com.klu.OnlineMedicalAppointment.model.Payment;
import com.klu.OnlineMedicalAppointment.service.PaymentServiceImplementation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.razorpay.Order;
import com.razorpay.RazorpayException;

@RestController
@CrossOrigin(origins = {"https://sdp-java.vercel.app", "https://sdp2200030709.netlify.app"}, allowCredentials = "true")
public class PaymentController {

    @Autowired
    private PaymentServiceImplementation paymentService;

    @PutMapping("/payments/payNow")
    public Payment payNow(@RequestBody Payment payment) {
        payment.setIsPaid(true); 
        paymentService.createPayment(payment); 
        return payment;
    }

    @PostMapping(value = "/payments/createOrder", produces = "application/json")
    @ResponseBody
    public Map<String, Object> createOrder(@RequestBody Payment payment) throws RazorpayException {
    	
        Order order = paymentService.createRazorpayOrder(payment.getAmount());

        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("status", order.get("status"));
        

        return response;
    }
    
    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    private final RestTemplate restTemplate;

    public PaymentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/payments/paymentDetails/{paymentId}")
    public ResponseEntity<Object> getPaymentDetails(@PathVariable String paymentId) {
        String url = "https://api.razorpay.com/v1/payments/" + paymentId;

        
        String auth = razorpayKey + ":" + razorpaySecret;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
    }

}

