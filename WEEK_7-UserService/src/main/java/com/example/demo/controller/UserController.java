package com.example.demo.controller;



import org.springframework.web.bind.annotation.*;

import com.example.demo.model.User;

import java.util.*;



@RestController
@RequestMapping("/users")
public class UserController {

    List<User> users = new ArrayList<>();

    @PostMapping
    public String addUser(@RequestBody User user) {
        users.add(user);
        return "User added";
    }

    @GetMapping
    public List<User> getUsers() {
        return users;
    }
}
