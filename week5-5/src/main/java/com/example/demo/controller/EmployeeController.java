package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // GET /api/employees/paginate?pageNo=0&pageSize=3
    @GetMapping("/paginate")
    public ResponseEntity<Map<String, Object>> getWithPagination(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize) {

        Page<Employee> page = employeeService.getEmployeesWithPagination(pageNo, pageSize);
        return buildResponse(page);
    }

    // GET /api/employees/sort?sortField=salary&sortDir=desc
    @GetMapping("/sort")
    public ResponseEntity<Map<String, Object>> getSorted(
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<Employee> page = employeeService.getEmployeesSortedBy(sortField, sortDir);
        return buildResponse(page);
    }

    // GET /api/employees?pageNo=0&pageSize=3&sortField=salary&sortDir=desc
    @GetMapping
    public ResponseEntity<Map<String, Object>> getWithPaginationAndSorting(
            @RequestParam(defaultValue = "0")    int    pageNo,
            @RequestParam(defaultValue = "3")    int    pageSize,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc")  String sortDir) {

        Page<Employee> page = employeeService
                .getEmployeesWithPaginationAndSorting(pageNo, pageSize, sortField, sortDir);
        return buildResponse(page);
    }

    // GET /api/employees/multi-sort?pageNo=0&pageSize=5
    @GetMapping("/multi-sort")
    public ResponseEntity<Map<String, Object>> getMultiSorted(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {

        Page<Employee> page = employeeService.getEmployeesSortedByMultipleFields(pageNo, pageSize);
        return buildResponse(page);
    }

    // GET /api/employees/department/IT?pageNo=0&pageSize=3&sortField=salary&sortDir=desc
    @GetMapping("/department/{dept}")
    public ResponseEntity<Map<String, Object>> getByDepartment(
            @PathVariable String dept,
            @RequestParam(defaultValue = "0")       int    pageNo,
            @RequestParam(defaultValue = "3")       int    pageSize,
            @RequestParam(defaultValue = "salary")  String sortField,
            @RequestParam(defaultValue = "desc")    String sortDir) {

        Page<Employee> page = employeeService
                .getEmployeesByDepartment(dept, pageNo, pageSize, sortField, sortDir);
        return buildResponse(page);
    }

    // ─── Helper: Build consistent paginated response ──────────────────────────
    private ResponseEntity<Map<String, Object>> buildResponse(Page<Employee> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("data",          page.getContent());
        response.put("currentPage",   page.getNumber());
        response.put("pageSize",      page.getSize());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages",    page.getTotalPages());
        response.put("isFirst",       page.isFirst());
        response.put("isLast",        page.isLast());
        return ResponseEntity.ok(response);
    }
}
