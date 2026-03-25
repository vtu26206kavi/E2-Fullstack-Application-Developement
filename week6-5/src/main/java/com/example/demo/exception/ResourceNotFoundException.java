package com.example.demo.exception;


public class ResourceNotFoundException extends RuntimeException {

    private String details;

    public ResourceNotFoundException(String message, String details) {
        super(message);
        this.details = details;
    }

    public String getDetails() { return details; }
}