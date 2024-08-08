package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
                        .allowedOrigins("*") // 모든 도메인 허용
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD") // 허용할 HTTP 메서드
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(false) // 인증 정보 허용 여부
                        .maxAge(3600); // pre-flight 요청 캐시 시간
            }
        };
    }
}
