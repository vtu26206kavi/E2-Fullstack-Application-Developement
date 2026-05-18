package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.JobService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final JobService jobService;
    
    public HomeController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @RequestParam(required = false) String search) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPLOYER"))) {
            return "redirect:/jobs/employer";
        }
        List<Job> jobs;
        if (search != null && !search.isEmpty()) {
            jobs = jobService.searchJobs(search);
        } else {
            jobs = jobService.findAllJobs();
        }
        model.addAttribute("jobs", jobs);
        return "dashboard";
    }
}
