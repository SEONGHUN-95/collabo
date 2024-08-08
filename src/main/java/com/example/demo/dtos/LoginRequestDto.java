package com.example.demo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {

    @NotNull(message = "이메일 입력은 필수입니다.")
    @Email
    private String email;


    @NotNull(message = "패스워드 입력은 필수입니다.")
    private String password;
}
