package com.example.shopproject.util.rabbitmq;


import com.example.shopproject.util.email.EmailSendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailSendingService emailSendingService;

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void receive(String message) {
        // xabarni ojrotib  formatini: "to;subject;body"
        String[] parts = message.split(";", 3);
        String to = parts[0];
        String subject = parts[1];
        String body = parts[2];

        emailSendingService.sendSimpleEmail(to, subject, body);
    }
}