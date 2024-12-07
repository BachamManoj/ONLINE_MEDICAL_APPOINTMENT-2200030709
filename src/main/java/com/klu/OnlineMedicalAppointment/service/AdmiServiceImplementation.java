package com.klu.OnlineMedicalAppointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Admin;
import com.klu.OnlineMedicalAppointment.repository.AdminRepository;

@Service
public class AdmiServiceImplementation implements AdminService {

	@Autowired 
	private AdminRepository adminRepository;
	
	@Override
	public Admin checkLogin(String email,String password) {
		return adminRepository.checkAdminLogin(email, password);
	}

}
