package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class Student {

    private String name = "Kavi Shree";

    // ✅ Correct Constructor (No return type)
    public Student() {
        System.out.println("Called constructor");
    }

    public String getName() {
        return name;
    }
}