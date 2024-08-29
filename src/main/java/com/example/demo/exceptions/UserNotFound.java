package com.example.demo.exceptions;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super(message);
    }
    public UserNotFound(){
        super("user not found");
    }
}



