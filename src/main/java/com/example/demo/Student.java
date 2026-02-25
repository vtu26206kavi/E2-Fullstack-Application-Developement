package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String department;

    private int age;

    public Student() {}

    public Student(String name, String department, int age,long id) {
        this.name = name;
        this.department = department;
        this.age = age;
        this.id=id;
    }

   
    public long getId() { return id; }

    public String getName() { return name; }

    public String getDepartment() { return department; }

    public int getAge() { return age; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDepartment(String department) { this.department = department; }

    public void setAge(int id2) { this.age = id2; }
}