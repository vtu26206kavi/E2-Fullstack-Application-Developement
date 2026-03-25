package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User saved = userService.registerUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)    // 201
                .body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Dummy credential check (replace with DB lookup in production)
        if ("admin".equals(username) && "password123".equals(password)) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity
                    .status(HttpStatus.OK)       // 200
                    .body(new LoginResponse(token, "Login successful"));
        }

        // Invalid credentials
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                "Invalid credentials",
                "Username or password is incorrect"
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)  // 401
                .body(error);
    }
}