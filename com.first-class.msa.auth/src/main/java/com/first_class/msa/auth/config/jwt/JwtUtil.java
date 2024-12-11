package com.first_class.msa.auth.config.jwt;

import com.first_class.msa.auth.domain.model.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${service.jwt.secret-key}")
    private String secretKey;

    // JWT 토큰 생성
    public String generateToken(Long userId, String account, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 1일 만료 시간

        String token = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("account", account)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        return "Bearer " + token;
    }

    // JWT 토큰에서 사용자 계정 추출
    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // account 클레임 추출
    public String extractAccount(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("account", String.class);
    }

    //권한 추출
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // JWT 토큰 유효성 검증
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    // JWT 토큰 검증
    public boolean validateToken(String token, String account) {
        return (account.equals(extractAccount(token)) && !isTokenExpired(token));
    }
}
