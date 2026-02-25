package com.example.demo;



import org.springframework.stereotype.Component;

@Component("smsService")
public class SMSService implements NotificationService {

    public void send(String message) {
        System.out.println("SMS Sent: " + message);
    }
}
