package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.klu.OnlineMedicalAppointment.model.Message;
import com.klu.OnlineMedicalAppointment.repository.MessageRepository;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    private final SimpMessagingTemplate template;

    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        messageRepository.save(message); 
        template.convertAndSendToUser(message.getReceiver(), "/topic/messages", message);
        return message;
    }
}
