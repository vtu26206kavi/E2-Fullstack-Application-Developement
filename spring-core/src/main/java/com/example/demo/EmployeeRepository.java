package com.example.demo;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeRepository {

    private List<Employee> employeeList = new ArrayList<>();

    public void save(Employee emp) {
        employeeList.add(emp);
    }

    public List<Employee> findAll() {
        return employeeList;
    }
}
