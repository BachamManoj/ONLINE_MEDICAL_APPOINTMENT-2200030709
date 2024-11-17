package com.klu.OnlineMedicalAppointment.service;

import com.klu.OnlineMedicalAppointment.model.Medicine;
import com.klu.OnlineMedicalAppointment.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineServiceImplementation implements MedicineService{

    @Autowired
    private MedicineRepository medicineRepository;

    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

   
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

  
    public Optional<Medicine> getMedicineById(Long id) {
        return medicineRepository.findById(id);
    }

  
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
}
