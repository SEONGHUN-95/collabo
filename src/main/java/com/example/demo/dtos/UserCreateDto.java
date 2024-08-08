package com.example.demo.dtos;

public record UserCreateDto(
        String username,
        String email,
        String password
) {
}
