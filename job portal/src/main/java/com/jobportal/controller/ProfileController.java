package com.jobportal.controller;

import com.jobportal.entity.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ProfileController {

    private final UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/uploadResume")
    public String uploadResume(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (file.isEmpty()) {
            return "redirect:/profile?error=Empty file";
        }
        try {
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, file.getBytes());

            User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            user.setResumeFileName(fileName);
            userService.updateUser(user);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/profile?error=Upload failed";
        }
        return "redirect:/profile?success";
    }
}
