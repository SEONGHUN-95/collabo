package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDto {
        private String title;          // 제목을 수정할 수 있도록 선택적으로 포함
        private String content;        // 내용을 수정할 수 있도록 선택적으로 포함
//        private List<MultipartFile> newImages;
//        private List<Long> deleteImageIds;
}
