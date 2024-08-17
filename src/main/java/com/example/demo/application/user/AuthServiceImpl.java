package com.example.demo.application.user;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LogoutRequestDto;
import com.example.demo.dtos.TokenDto;
import com.example.demo.exceptions.TokenNotMatched;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public TokenDto login(LoginRequestDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );
            String accessToken = jwtUtil.createAccessToken(authentication);
            String refreshToken = jwtUtil.createRefreshToken(authentication);

            // Redis에 Refresh Token 저장
            redisTemplate.opsForValue().set(authentication.getName(), refreshToken, jwtUtil.getRefreshTokenExpiry(), TimeUnit.MILLISECONDS);

            return new TokenDto(accessToken, refreshToken);
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            e.printStackTrace(); // 상세한 스택 트레이스 출력
            throw new RuntimeException(e); // 원래 예외를 감싸서 던짐
        }
    }

    public TokenDto reissueToken(String refreshToken) {
        jwtUtil.validateToken(refreshToken);
        Authentication authentication = jwtUtil.getAuthentication(refreshToken);

        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());

        if (!redisRefreshToken.equals(refreshToken)) {
            throw new TokenNotMatched();
        }
        TokenDto tokenDto = new TokenDto(
                jwtUtil.createAccessToken(authentication),
                refreshToken
        );
        return tokenDto;
    }

    @Transactional
    public void logout(LogoutRequestDto dto) {
        Authentication authentication = jwtUtil.getAuthentication(dto.getRefreshToken());

        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());

        if (redisRefreshToken == null) {
            throw new IllegalStateException("사용자의 토큰을 이미 redis에서 찾을 수 없습니다..");
        }

        redisTemplate.delete(authentication.getName());
    }


}