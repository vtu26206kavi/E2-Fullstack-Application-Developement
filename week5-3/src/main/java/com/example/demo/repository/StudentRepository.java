package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository provides all CRUD methods automatically
    // save(), findAll(), findById(), deleteById() etc.
}
