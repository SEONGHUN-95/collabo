package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateDto(
        @NotBlank
        String content
) {
}
