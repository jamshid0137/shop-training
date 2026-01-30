package com.example.shopproject.util.rabbitmq;



import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(String to, String subject, String body) {
        // Xabar formatini oddiy qilib; ";" bilan ajratamiz
        String message = to + ";" + subject + ";" + body;
        rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_QUEUE, message);
        System.out.println("Email message sent to queue for " + to);
    }
}