package com.example.shopproject.util.email;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Controller {
    @Autowired
    private final EmailSendingService emailSendingService;

    public Controller(EmailSendingService emailSendingService) {
        this.emailSendingService = emailSendingService;
    }

    @GetMapping("/email")
    public void sendMail(){
        emailSendingService.sendSimpleEmail("jmadaminov.unlimited@gmail.com","Test","Assalomu aleykum !");
    }
}