package com.example.demo.StudentCRUD.controller;
import com.example.demo.StudentCRUD.model.Student;
import com.example.demo.StudentCRUD.service.studentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
public class studentController {
    @Autowired
    studentService studentService;
    @GetMapping("/students")
    public List<Student> getStudents(){
        return studentService.getStudents();
    }
    @GetMapping("students/{rno}")

    public Student getStudentbyrno(@PathVariable("rno")int rollnumber){
        return studentService.getStudentbyrno(rollnumber);
    }
    @PostMapping("/students")
    public String addStudent(@RequestBody Student student){
        studentService.addStudent(student);
        return "Sucess";
    }
    @PutMapping("/students")
    public String updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);

    }
    @DeleteMapping("/students/{rno}")
    public String deleteStudent(@PathVariable int rno){
        return studentService.deleteStudent(rno);

    }

}
