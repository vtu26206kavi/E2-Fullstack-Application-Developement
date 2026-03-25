package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // ── CREATE ────────────────────────────────────────────────────────────────
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // ── READ ALL ──────────────────────────────────────────────────────────────
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ── READ BY ID ────────────────────────────────────────────────────────────
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public Student updateStudent(Long id, Student updatedStudent) {
        Optional<Student> existing = studentRepository.findById(id);
        if (existing.isPresent()) {
            Student student = existing.get();
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            student.setEmail(updatedStudent.getEmail());
            student.setAge(updatedStudent.getAge());
            student.setDepartment(updatedStudent.getDepartment());
            student.setGpa(updatedStudent.getGpa());
            return studentRepository.save(student);
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public String deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return "Student with id " + id + " deleted successfully.";
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }
}