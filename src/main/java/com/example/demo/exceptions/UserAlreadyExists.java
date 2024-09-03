package com.example.demo.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists(String s, DataIntegrityViolationException e) {
    }
}
