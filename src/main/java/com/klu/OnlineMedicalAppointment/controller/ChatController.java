package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.klu.OnlineMedicalAppointment.model.Message;
import com.klu.OnlineMedicalAppointment.repository.MessageRepository;

@Controller
public class ChatController {

    private final SimpMessagingTemplate template;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate template, MessageRepository messageRepository) {
        this.template = template;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(Message message) {
        messageRepository.save(message);

        List<Message> updatedChatHistory = messageRepository.findChatHistory(message.getSender(), message.getReceiver());

        template.convertAndSendToUser(message.getSender(), "/queue/history", updatedChatHistory);
        template.convertAndSendToUser(message.getReceiver(), "/queue/history", updatedChatHistory);
    }
}
