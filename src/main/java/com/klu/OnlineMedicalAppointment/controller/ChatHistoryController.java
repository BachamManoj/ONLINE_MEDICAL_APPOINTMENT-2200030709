package com.klu.OnlineMedicalAppointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import com.klu.OnlineMedicalAppointment.model.Message;
import com.klu.OnlineMedicalAppointment.repository.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    @Autowired
    private MessageRepository messageRepository;
   
    @GetMapping("/history")
    @ResponseBody
    public List<Message> getChatHistory(@RequestParam String sender, @RequestParam String receiver) {
        return messageRepository.findChatHistory(sender, receiver);
    }
}
