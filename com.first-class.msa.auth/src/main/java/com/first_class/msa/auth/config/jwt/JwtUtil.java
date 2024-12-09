package com.first_class.msa.auth.config.jwt;

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
    public String generateToken(String account) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 1일 만료 시간

        return Jwts.builder()
                .setSubject(account)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // JWT 토큰에서 사용자 계정 추출
    public String extractAccount(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
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
