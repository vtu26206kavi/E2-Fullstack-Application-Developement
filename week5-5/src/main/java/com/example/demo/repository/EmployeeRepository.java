package com.example.demo.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Pagination + Sorting by department (custom query method)
    Page<Employee> findByDepartment(String department, Pageable pageable);

    // Pagination + Sorting by salary range
    Page<Employee> findBySalaryBetween(double minSalary, double maxSalary, Pageable pageable);
}