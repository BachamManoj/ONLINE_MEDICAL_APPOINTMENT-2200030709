package com.klu.OnlineMedicalAppointment.repository;


import com.klu.OnlineMedicalAppointment.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
}
