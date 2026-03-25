package com.example.demo.model;



import jakarta.validation.constraints.*;

public class User {

    private int id;

    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotNull(message = "Age must not be null")
    @Min(value = 18, message = "Age must be at least 18")
    private Integer age;

    // Default constructor
    public User() {}

    public User(int id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
