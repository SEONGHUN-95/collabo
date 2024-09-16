package com.example.demo.controllers;

import com.example.demo.application.user.AuthServiceImpl;
import com.example.demo.application.user.UserService;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LogoutRequestDto;
import com.example.demo.dtos.PasswordUpdateDto;
import com.example.demo.dtos.RefreshTokenRequestDto;
import com.example.demo.dtos.TokenDto;
import com.example.demo.dtos.UserCreateDto;
import com.example.demo.dtos.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<TokenDto> authenticate(@Valid @RequestBody LoginRequestDto request) {
        TokenDto token = authServiceImpl.login(request);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "액세스 토큰 재발급", description = "Redis에 저장된 RefreshToken과 클라이언트의 RefreshToken을 대조하고 AT를 발급합니다.")
    @PostMapping("/token/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody RefreshTokenRequestDto request) {
        TokenDto tokenDto = authServiceImpl.reissueToken(request.refreshToken());
        return ResponseEntity.ok(tokenDto);
    }


    @Operation(summary = "로그아웃", description = "클라이언트에 저장된 RT를 보내어 Redis에 저장된 RT 삭제합니다.")
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequestDto request) {
        authServiceImpl.logout(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 정보 불러오기", description = "내 정보 보여주기")
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getMyProfile(Authentication authentication) {
        UserDto userDto = userService.getUserProfile(authentication.getName());
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "사용자 정보 불러오기", description = "해당 사용자 정보 보기")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable Long userId) {
        UserDto userDto = userService.getUserProfile(userId);
        return ResponseEntity.ok(userDto);
    }


    @Operation(summary = "사용자 이름, 프로필 사진 변경", description = "둘 중 하나만 올려도 변경 가능.")
    @PatchMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateProfile(@RequestParam(value = "name", required = false) String name,
                                           @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                           Authentication authentication) {
        userService.updateUserProfile(authentication.getName(), name, profileImage);
        return ResponseEntity.ok("프로필 변경 완료");
    }

    @Operation(summary = "사용자 이름 중복 확인", description = "중복 확인 후 이름 변경 가능하게끔 구현 필요")
    @GetMapping("/check-name")
    public ResponseEntity<String> checkNameAvailability(@RequestParam String name) {
        userService.checkNameAvailability(name);
        return ResponseEntity.ok("사용 가능한 이름입니다.");
    }

    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호, 변경하고자 하는 비밀번호, 비밀번호 확인 대조 후 변경")
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateDto passwordUpdateDto, Authentication authentication) {
        userService.updatePassword(authentication.getName(), passwordUpdateDto);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }
}
