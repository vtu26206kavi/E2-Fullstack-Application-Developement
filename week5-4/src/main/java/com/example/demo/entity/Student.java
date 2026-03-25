package com.example.demo.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data                    
@NoArgsConstructor      
@AllArgsConstructor      
@Builder                 
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private int age;

    @Column(unique = true, nullable = false)
    private String email;
}
