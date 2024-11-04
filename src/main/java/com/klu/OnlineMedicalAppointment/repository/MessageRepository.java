package com.klu.OnlineMedicalAppointment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klu.OnlineMedicalAppointment.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(String sender, String receiver);
}
