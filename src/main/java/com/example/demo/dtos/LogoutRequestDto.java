package com.example.demo.dtos;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String refreshToken;
}
