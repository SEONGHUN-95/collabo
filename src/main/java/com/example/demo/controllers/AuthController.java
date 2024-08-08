package com.example.demo.controllers;

import com.example.demo.application.user.AuthServiceImpl;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LogoutRequestDto;
import com.example.demo.dtos.RefreshTokenRequestDto;
import com.example.demo.dtos.TokenDto;
import com.example.demo.exceptions.TokenNotMatched;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Log4j2
public class AuthController {
    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody LoginRequestDto request,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            String errorName = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(errorName);
            return ResponseEntity.badRequest().body(errorName);
        }

        try {
            TokenDto token = this.authServiceImpl.login(request);
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID_CREDENTIALS");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody RefreshTokenRequestDto request) {
        try {
            TokenDto tokenDto = authServiceImpl.reissueToken(request.refreshToken());
            return ResponseEntity.ok(tokenDto);
        } catch (TokenNotMatched e) {
            // 리프레시 토큰이 유효하지 않으면 401 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @Valid @RequestBody LogoutRequestDto request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorName = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(errorName);
            return ResponseEntity.badRequest().body(errorName);
        }

        try {
            authServiceImpl.logout(request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("TOKEN DELETED");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
