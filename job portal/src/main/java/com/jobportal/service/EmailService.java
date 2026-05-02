package com.jobportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationOTP(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for Job Portal Registration");
            message.setText("Welcome to the Job Portal!\n\nYour OTP for registration is: " + otp + "\n\nIf you did not request this, please ignore this email.");
            
            // mailSender.send(message); 
            // In a real environment, uncomment the above line to send the email.
            
            logger.info("==========================================");
            logger.info("MOCK EMAIL SENT TO: {}", toEmail);
            logger.info("SUBJECT: {}", message.getSubject());
            logger.info("BODY:\n{}", message.getText());
            logger.info("==========================================");

        } catch (Exception e) {
            logger.error("Failed to send email to {}", toEmail, e);
        }
    }
}
