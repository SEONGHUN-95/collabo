package com.example.demo.dtos;


public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
