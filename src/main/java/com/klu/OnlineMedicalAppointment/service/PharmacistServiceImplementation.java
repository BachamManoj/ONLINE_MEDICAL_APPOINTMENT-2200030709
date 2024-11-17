package com.klu.OnlineMedicalAppointment.service;

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

}
