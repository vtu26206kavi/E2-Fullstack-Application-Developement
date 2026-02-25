package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    
    @PostMapping("/put")
    public Student create(@RequestBody Student student) {
        return service.save(student);
    }

   
    @GetMapping
    public List<Student> getAll() {
        return service.getAll();
    }

  
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return service.getById(id);
    }

 
    


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "Student deleted successfully!";
    }


    @GetMapping("/department/{dept}")
    public List<Student> byDepartment(@PathVariable String dept) {
        return service.getByDepartment(dept);
    }

  
    @GetMapping("/page")
    public Page<Student> paginate(
            @RequestParam int page,
            @RequestParam int size) {
        return service.getPaginated(page, size);
    }
}