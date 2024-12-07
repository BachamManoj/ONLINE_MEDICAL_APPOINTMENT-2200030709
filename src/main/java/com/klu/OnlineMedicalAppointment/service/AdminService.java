package com.klu.OnlineMedicalAppointment.service;

import com.klu.OnlineMedicalAppointment.model.Admin;

public interface AdminService {
	public Admin checkLogin(String email,String password);
}
