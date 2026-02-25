package com.example.demo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByDepartment(String department);

    List<Student> findByAgeGreaterThan(int age);

    Page<Student> findByDepartment(String department, Pageable pageable);
}