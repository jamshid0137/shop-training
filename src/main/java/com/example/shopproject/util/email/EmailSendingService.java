package com.example.shopproject.util.email;




import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailSendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;//email usernameni olvolish.
    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    private JavaMailSender javaMailSender;





    public void sendMimeEmail(String email, String subject, String body) {//subject titlesi,body-ichi xatni.

        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            msg.setFrom(fromAccount);

            MimeMessageHelper helper = new MimeMessageHelper(msg, true);//true fayllar bn degani
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);//bodyni html formatdan parse qilib ishlat

            CompletableFuture.runAsync(() -> {
                javaMailSender.send(msg);
            });//har bir user uchun alohida thread yaratib asosiy threaddan ajratib responseni tez yuboradigan qilamiz


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    public void sendSimpleEmail(String email, String subject, String body) {//subject titlesi,body-ichi xatni.
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromAccount);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);


    }
}
