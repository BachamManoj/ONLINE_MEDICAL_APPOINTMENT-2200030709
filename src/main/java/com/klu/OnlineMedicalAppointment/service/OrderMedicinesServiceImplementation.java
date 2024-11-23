package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.OrderMedicines;
import com.klu.OnlineMedicalAppointment.repository.OrderMedicinesRepository;

@Service
public class OrderMedicinesServiceImplementation implements OrderMedicinesService {

	@Autowired
	private OrderMedicinesRepository orderMedicinesRepository;
	
	@Override
	public void createOrder(OrderMedicines orderMedicines) {
		orderMedicinesRepository.save(orderMedicines);
	}

	@Override
	public OrderMedicines confirmPayment(Long id) {
		OrderMedicines orderMedicines=orderMedicinesRepository.findByAppointmentId(id);
		orderMedicines.setIsPaid(true);
		return orderMedicinesRepository.save(orderMedicines);
	}

	@Override
	public List<OrderMedicines> getAllOrders() {
		
		return orderMedicinesRepository.findAll();
	}

	@Override
	public OrderMedicines confirmOrder(Long id) {
		OrderMedicines orderMedicines=orderMedicinesRepository.findByAppointmentId(id);
		orderMedicines.setAccept(true);
		return orderMedicinesRepository.save(orderMedicines);
	}

}
