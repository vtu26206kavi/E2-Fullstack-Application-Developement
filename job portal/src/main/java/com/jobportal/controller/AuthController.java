package com.jobportal.controller;

import com.jobportal.entity.User;
import com.jobportal.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    
    private final UserService userService;
    private final com.jobportal.service.EmailService emailService;
    
    public AuthController(UserService userService, com.jobportal.service.EmailService emailService) {
         this.userService = userService;
         this.emailService = emailService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        
        // Generate a simple 6-digit mock OTP
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));
        emailService.sendRegistrationOTP(user.getUsername(), otp);
        
        return "redirect:/login?registered";
    }
}
