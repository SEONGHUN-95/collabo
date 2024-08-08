package com.example.demo.application.user;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.TokenDto;

public interface AuthService {
    TokenDto login(LoginRequestDto dto);
}
