package com.example.demo.controllers;

import com.example.demo.application.user.GetUserService;
import com.example.demo.application.user.UserService;
import com.example.demo.dtos.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserCreateDto userCreateDto){
        userService.createUser(userCreateDto.username(), userCreateDto.email(), userCreateDto.password());
    }

}
