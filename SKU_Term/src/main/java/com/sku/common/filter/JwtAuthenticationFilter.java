package com.sku.common.filter;

import com.sku.common.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1쿠키에서 Access Token 추출로 변경
        String token = resolveToken((HttpServletRequest) request);

        // 2토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // TODO: 만약 AccessToken이 만료되었다면, RefreshToken을 확인하고 재발급하는 로직은
        // 보통 클라이언트가 401을 받고 재요청하거나, 이곳에서 예외처리를 통해 처리합니다.
        // PPT 흐름상 여기서는 '유효한 토큰이 있으면 인증 처리'만 담당합니다.

        chain.doFilter(request, response);
    }

    // 쿠키에서 "accessToken" 이름의 값을 찾아 반환
    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}