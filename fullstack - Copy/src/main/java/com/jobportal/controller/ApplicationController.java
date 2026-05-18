package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final UserService userService;

    public ApplicationController(ApplicationService applicationService, JobService jobService, UserService userService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
        this.userService = userService;
    }

    @PostMapping("/jobs/{id}/apply")
    public String applyForJob(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            Job job = jobService.findJobById(id).orElseThrow();
            User applicant = userService.findByUsername(userDetails.getUsername()).orElseThrow();
            applicationService.applyForJob(job, applicant);
            return "redirect:/dashboard?applied";
        } catch (Exception e) {
            return "redirect:/jobs/" + id + "?error";
        }
    }

    @GetMapping("/applications/student")
    public String studentApplications(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User applicant = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("applications", applicationService.getApplicationsByApplicant(applicant));
        return "student_applications";
    }

    @GetMapping("/applications/employer/{jobId}")
    public String jobApplicants(@PathVariable Long jobId, Model model) {
        Job job = jobService.findJobById(jobId).orElseThrow();
        model.addAttribute("job", job);
        model.addAttribute("applications", applicationService.getApplicationsByJob(job));
        return "job_applicants";
    }

    @PostMapping("/applications/updateStatus")
    public String updateStatus(@RequestParam Long appId, @RequestParam String status, @RequestParam Long jobId) {
        applicationService.updateStatus(appId, status);
        return "redirect:/applications/employer/" + jobId + "?updated";
    }
}
