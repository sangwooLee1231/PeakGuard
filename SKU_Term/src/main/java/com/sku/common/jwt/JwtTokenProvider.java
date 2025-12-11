package com.sku.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    // 1시간 (밀리초)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60;

    // 6시간 (밀리초)
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 6;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        log.info("JWT Secret Key initialized. key length = {} bytes", keyBytes.length);
    }

    /** Access Token 생성 */
    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /** Refresh Token 생성 */
    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /** 공통 토큰 생성 로직 */
    private String createToken(Authentication authentication, long expireTime) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date validity = new Date(now + expireTime);

        String username = authentication.getName(); // 여기서는 studentNumber

        return Jwts.builder()
                .setSubject(username)                // 토큰 subject = 학번
                .claim("auth", authorities)          // 권한 정보
                .setIssuedAt(issuedAt)
                .setExpiration(validity)
                // ★ HS256 사용 (HS512 → HS256으로 변경해서 WeakKeyException 제거)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String authInfo = claims.get("auth", String.class);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (authInfo != null && !authInfo.isEmpty()) {
            authorities = Arrays.stream(authInfo.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        UserDetails principal = new User(
                claims.getSubject(),     // username = 학번
                "",                      // 비밀번호는 필요 없음
                authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    public String getStudentNumber(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다. message={}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다. message={}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다. message={}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다. message={}", e.getMessage());
        } catch (Exception e) {
            log.info("JWT 검증 실패: {}", e.getMessage());
        }
        return false;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
