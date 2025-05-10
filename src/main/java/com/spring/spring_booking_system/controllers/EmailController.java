package com.spring.spring_booking_system.controllers;

import com.spring.spring_booking_system.entities.EmailDetails;
import com.spring.spring_booking_system.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendmail")
    public ResponseEntity<EmailDetails> sendMail(@RequestBody EmailDetails details) {
        emailService.sendSimpleMail(details);

        return ResponseEntity.ok(details);
    }
}
