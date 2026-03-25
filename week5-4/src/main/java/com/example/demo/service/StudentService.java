package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ── CRUD Operations ──

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // ── Custom Query Operations ──

    public List<Student> getByDepartment(String department) {
        return studentRepository.findByDepartmentIgnoreCase(department);
    }

    public List<Student> getByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> getOlderThan(int age) {
        return studentRepository.findByAgeGreaterThan(age);
    }

    public List<Student> getByAgeRange(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public List<Student> getByDeptAndAgeRange(String dept, int min, int max) {
        return studentRepository.getStudentsByDeptAndAgeRange(dept, min, max);
    }

    public List<Student> searchByName(String keyword) {
        return studentRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<Object[]> getDeptCount() {
        return studentRepository.countStudentsPerDepartment();
    }

    public List<Object[]> getAvgAgePerDept() {
        return studentRepository.averageAgePerDepartment();
    }

    public Optional<Student> getYoungestInDept(String dept) {
        return studentRepository.findYoungestInDepartment(dept);
    }
}
