package com.jobportal.controller;

import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    public JobController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    @GetMapping("/employer")
    public String employerJobs(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User employer = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("jobs", jobService.findJobsByEmployer(employer));
        return "employer_dashboard";
    }

    @GetMapping("/post")
    public String showPostJobForm(Model model) {
        model.addAttribute("job", new Job());
        return "post_job";
    }

    @PostMapping("/post")
    public String postJob(@ModelAttribute("job") Job job, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User employer = userService.findByUsername(userDetails.getUsername()).orElseThrow();
        job.setEmployer(employer);
        jobService.saveJob(job);
        return "redirect:/jobs/employer?posted";
    }

    @GetMapping("/{id}")
    public String jobDetails(@PathVariable Long id, Model model) {
        Job job = jobService.findJobById(id).orElseThrow();
        model.addAttribute("job", job);
        return "job_details";
    }

    @GetMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return "redirect:/jobs/employer?deleted";
    }
}
