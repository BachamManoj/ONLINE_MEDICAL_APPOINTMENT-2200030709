package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import com.klu.OnlineMedicalAppointment.model.OrderMedicines;

public interface OrderMedicinesService {
	void createOrder(OrderMedicines orderMedicines);
	OrderMedicines confirmPayment(Long id);
	List<OrderMedicines> getAllOrders();
	OrderMedicines confirmOrder(Long id);
}
