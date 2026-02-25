package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public Student save(Student student) {
        return repository.save(student);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student getById(long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public List<Student> getByDepartment(String dept) {
        return repository.findByDepartment(dept);
    }

    public Page<Student> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return repository.findAll(pageable);
    }
}