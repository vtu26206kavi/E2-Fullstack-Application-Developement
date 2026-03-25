package com.example.demo.service;
	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

	@Service
	public class EmployeeService {

	    @Autowired
	    private EmployeeRepository employeeRepository;

	    // ─── PAGINATION ONLY ───────────────────────────────────────────────────────

	    public Page<Employee> getEmployeesWithPagination(int pageNo, int pageSize) {
	        Pageable pageable = PageRequest.of(pageNo, pageSize);
	        return employeeRepository.findAll(pageable);
	    }

	    // ─── SORTING ONLY ──────────────────────────────────────────────────────────

	    public Page<Employee> getEmployeesSortedBy(String sortField, String sortDir) {
	        Sort sort = sortDir.equalsIgnoreCase("asc")
	                ? Sort.by(sortField).ascending()
	                : Sort.by(sortField).descending();

	        // Returns all records sorted (page 0, large size = effectively all)
	        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
	        return employeeRepository.findAll(pageable);
	    }

	    // ─── PAGINATION + SORTING COMBINED ─────────────────────────────────────────

	    public Page<Employee> getEmployeesWithPaginationAndSorting(
	            int pageNo, int pageSize, String sortField, String sortDir) {

	        Sort sort = sortDir.equalsIgnoreCase("asc")
	                ? Sort.by(sortField).ascending()
	                : Sort.by(sortField).descending();

	        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
	        return employeeRepository.findAll(pageable);
	    }

	    // ─── MULTI-FIELD SORTING ───────────────────────────────────────────────────

	    public Page<Employee> getEmployeesSortedByMultipleFields(int pageNo, int pageSize) {
	        Sort sort = Sort.by(
	                Sort.Order.asc("department"),   // First: sort by department A→Z
	                Sort.Order.desc("salary")       // Then: sort by salary high→low
	        );
	        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
	        return employeeRepository.findAll(pageable);
	    }

	    // ─── FILTER + PAGINATION + SORTING ─────────────────────────────────────────

	    public Page<Employee> getEmployeesByDepartment(
	            String department, int pageNo, int pageSize, String sortField, String sortDir) {

	        Sort sort = sortDir.equalsIgnoreCase("asc")
	                ? Sort.by(sortField).ascending()
	                : Sort.by(sortField).descending();

	        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
	        return employeeRepository.findByDepartment(department, pageable);
	    }
	}