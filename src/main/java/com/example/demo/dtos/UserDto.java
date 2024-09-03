package com.example.demo.dtos;

import java.util.List;

public record UserDto(
        String email,
        String username,
        String profileImage,
        List<String> followers,
        List<String> following
) {
}
