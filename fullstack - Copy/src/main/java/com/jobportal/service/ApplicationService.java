package com.jobportal.service;

import com.jobportal.entity.JobApplication;
import com.jobportal.entity.Job;
import com.jobportal.entity.User;
import com.jobportal.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;

    public ApplicationService(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public JobApplication applyForJob(Job job, User applicant) {
        Optional<JobApplication> existing = applicationRepository.findByJobAndApplicant(job, applicant);
        if (existing.isPresent()) {
            throw new RuntimeException("Already applied for this job");
        }
        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(applicant);
        return applicationRepository.save(application);
    }

    public List<JobApplication> getApplicationsByApplicant(User applicant) {
        return applicationRepository.findByApplicant(applicant);
    }

    public List<JobApplication> getApplicationsByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    public void updateStatus(Long id, String status) {
        JobApplication application = applicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        applicationRepository.save(application);
    }
}
