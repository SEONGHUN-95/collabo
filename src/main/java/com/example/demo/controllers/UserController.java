package com.example.demo.controllers;

import com.amazonaws.services.cloudformation.model.NameAlreadyExistsException;
import com.example.demo.application.user.AuthServiceImpl;
import com.example.demo.application.user.UserService;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LogoutRequestDto;
import com.example.demo.dtos.PasswordUpdateDto;
import com.example.demo.dtos.RefreshTokenRequestDto;
import com.example.demo.dtos.TokenDto;
import com.example.demo.dtos.UserCreateDto;
import com.example.demo.dtos.UserDto;
import com.example.demo.exceptions.InvalidPassword;
import com.example.demo.exceptions.PasswordMismatch;
import com.example.demo.exceptions.TokenNotMatched;
import com.example.demo.exceptions.UserNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "회원가입/로그인/로그아웃/토큰 재발급")
@Log4j2
public class UserController {
    private final UserService userService;
    private final AuthServiceImpl authServiceImpl;

    @Operation(summary = "회원가입", description = "사용자 이름, 이메일, 비밀번호")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserCreateDto userCreateDto) {
        userService.createUser(userCreateDto.username(), userCreateDto.email(), userCreateDto.password());
    }

    @Operation(summary = "로그인", description = "사용자를 인증 후 AT, RT을 발급합니다.")
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디, 비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @Operation(summary = "액세스 토큰 재발급", description = "Redis에 저장된 RefreshToken과 클라이언트의 RefreshToken을 대조하고 AT를 발급합니다.")
    @PostMapping("/token/reissue")
    public ResponseEntity<?> reissue(@RequestBody RefreshTokenRequestDto request) {
        try {
            TokenDto tokenDto = authServiceImpl.reissueToken(request.refreshToken());
            return ResponseEntity.ok(tokenDto);
        } catch (TokenNotMatched e) {
            // 리프레시 토큰이 유효하지 않으면 401 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @Operation(summary = "로그아웃", description = "클라이언트에 저장된 RT를 보내어 Redis에 저장된 RT 삭제합니다. 클라이언트에서도 RT 삭제가 필요합니다.")
    @DeleteMapping("/logout")
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

    @Operation(summary = "사용자 정보 불러오기", description = "내 정보 보여주기")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            UserDto userDto = userService.getUserProfile(currentUsername);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾지 못했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류 발생");
        }
    }

    @Operation(summary = "사용자이름, 프로필 사진 변경", description = "이름은 JSON, 프사는 multipart로 전송 필요 둘 중 하나만 보내도 변경 가능")
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam(value = "name", required = false) String name,
                                           @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                           Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            userService.updateUserProfile(currentUsername, name, profileImage);
            return ResponseEntity.status(HttpStatus.OK).body("프로필 변경 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류 발생");
        }
    }

    @Operation(summary = "사용자 이름 중복 확인", description = "중복 확인 후 이름 변경 가능하게끔 구현 필요 (JSON 아니고 파라미터로 전송)")
    @GetMapping("/check-name")
    public ResponseEntity<?> checkNameAvailability(@RequestParam String name) {
        try {
            userService.checkNameAvailability(name);
            return ResponseEntity.ok().body("사용 가능한 이름입니다.");
        } catch (NameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 이름입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류");
        }
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호, 변경하고자 하는 비밀번호, 비밀번호 확인 대조 후 변경")
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateDto passwordUpdateDto, Authentication authentication) {
        try {
            userService.updatePassword(authentication.getName(), passwordUpdateDto);
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경 완료");
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (InvalidPassword e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        } catch (PasswordMismatch e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새로운 비밀번호가 서로 일치하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }
}
