package com.example.demo;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    // Using Qualifier
    @Autowired
    @Qualifier("emailService")
    private NotificationService notificationService;

    // Optional injection
    @Autowired(required = false)
    @Qualifier("smsService")
    private NotificationService optionalService;

    public void addEmployee(int id, String name) {
        Employee emp = new Employee(id, name);
        repository.save(emp);

        notificationService.send("Employee Added: " + name);

        if (optionalService != null) {
            optionalService.send("Optional SMS Notification: " + name);
        }
    }

    public void displayEmployees() {
        repository.findAll().forEach(System.out::println);
    }
}