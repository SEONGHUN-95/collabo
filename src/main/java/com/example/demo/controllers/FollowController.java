package com.example.demo.controllers;

import com.example.demo.application.user.FollowService;
import com.example.demo.exceptions.UserNotFound;
import com.example.demo.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "팔로우/언팔로우 관련 API")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "사용자 팔로우", description = "targetUserId -> 팔로우 대상")
    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<?> followUser(@PathVariable Long targetUserId, Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            followService.followUser(userDetails.getId(), targetUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body("사용자를 성공적으로 팔로우했습니다.");
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "사용자 언팔로우", description = "현재 사용자가 특정 사용자를 언팔로우합니다.")
    @DeleteMapping("/{targetUserId}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable Long targetUserId, Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            followService.unfollowUser(userDetails.getId(), targetUserId);
            return ResponseEntity.ok("사용자를 성공적으로 언팔로우했습니다.");
        } catch (UserNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
        }
    }


}
