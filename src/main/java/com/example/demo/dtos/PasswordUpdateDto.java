package com.example.demo.dtos;


public record PasswordUpdateDto(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
}
