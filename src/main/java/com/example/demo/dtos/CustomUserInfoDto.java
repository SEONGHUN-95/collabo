package com.example.demo.dtos;

import com.example.demo.models.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserInfoDto {
    private Long id;

    private String username;

    private String email;

    private String password;

    private RoleType role;

}
