package com.sku.member.controller;

import com.sku.member.dto.MemberLoginRequestDto;
import com.sku.member.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDto request, HttpServletResponse response) {
        // 1. 서비스 로그인 로직 수행
        Map<String, String> tokens = authService.login(request.getEmail(), request.getPassword());

        // 2. Access Token 쿠키 생성 (1시간)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokens.get("accessToken"))
                .httpOnly(true)          // JavaScript에서 접근 불가 (XSS 방지)
                .secure(false)           // HTTP에서도 전송 가능 (HTTPS 적용 시 true로 변경 필요)
                .path("/")               // 모든 경로에서 쿠키 전송
                .maxAge(60 * 60)         // 1시간 (3600초)
                .sameSite("Strict")      // CSRF 방지 강화
                .build();

        // 3. Refresh Token 쿠키 생성 (6시간)
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(60 * 60 * 6)
                .sameSite("Strict")
                .build();

        // 4. 응답 헤더에 쿠키 추가
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok("로그인 성공");
    }
}