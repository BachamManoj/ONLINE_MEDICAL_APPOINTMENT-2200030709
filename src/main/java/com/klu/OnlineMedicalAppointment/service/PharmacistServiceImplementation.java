package com.klu.OnlineMedicalAppointment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.OnlineMedicalAppointment.model.Pharmacist;
import com.klu.OnlineMedicalAppointment.repository.PharmacistRepository;

@Service
public class PharmacistServiceImplementation implements PharmacistService{

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Override
    public Pharmacist savePharmacist(Pharmacist pharmacist) {
        return pharmacistRepository.save(pharmacist);
    }
    
    @Override
    public Pharmacist findPharmacistByEmail(String email) {
        return pharmacistRepository.findByEmail(email);
    }

	@Override
	public List<Pharmacist> getAllPharmacist() {
		return pharmacistRepository.findAll();
	}

	@Override
	public void deletPharmasist(Long id) {
		pharmacistRepository.deleteById(id);	
	}
	
	

}
