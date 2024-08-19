package com.example.demo;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "헬스체크 API")
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "서버가 작동합니다.";
    }

}