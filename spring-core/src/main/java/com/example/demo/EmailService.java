package com.example.demo;
import org.springframework.stereotype.Component;

@Component("emailService")
public class EmailService implements NotificationService {

    public void send(String message) {
        System.out.println("Email Sent: " + message);
    }
}