package com.example.demo.service;




	import com.example.demo.exception.ResourceNotFoundException;
	import com.example.demo.model.User;
	import org.springframework.stereotype.Service;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.Optional;

	@Service
	public class UserService {

	    private List<User> userList = new ArrayList<>();
	    private int idCounter = 1;

	    // Register a new user
	    public User registerUser(User user) {
	        user.setId(idCounter++);
	        userList.add(user);
	        return user;
	    }

	    // Get user by ID — throws custom exception if not found
	    public User getUserById(int id) {
	        return userList.stream()
	                .filter(u -> u.getId() == id)
	                .findFirst()
	                .orElseThrow(() -> new ResourceNotFoundException(
	                        "User not found",
	                        "No user exists with ID: " + id
	                ));
	    }

	    // Get all users
	    public List<User> getAllUsers() {
	        return userList;
	    }
	}