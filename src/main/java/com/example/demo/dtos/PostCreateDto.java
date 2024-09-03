package com.example.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "게시글 생성 정보")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {
    private String title;
    private String content;
}
