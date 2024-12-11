package com.first_class.msa.gateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtAuthorizationFilter implements GlobalFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // 인증 제외 경로 처리
        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }

        // 토큰 추출
        String token = getJwtFromHeader(exchange);
        if (token == null || !validateToken(token)) {
            log.error("유효하지 않은 토큰입니다.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 사용자 정보 헤더 추가 및 요청 전달
        return chain.filter(addUserInfoHeader(exchange, token));
    }

    private boolean isExcludedPath(String path) {
        return path.equals("/auth/sign-in") || path.equals("/auth/sign-up");
    }

    private String getJwtFromHeader(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader; // Bearer 포함하여 반환
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            getClaimsJws(token.replace(BEARER_PREFIX, "")); // Bearer 제거 후 검증
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("유효하지 않은 JWT 서명입니다.", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰입니다.", e);
        }
        return false;
    }

    private ServerWebExchange addUserInfoHeader(ServerWebExchange exchange, String token) {
        Claims claims = getClaimsJws(token.replace(BEARER_PREFIX, "")).getBody();

        String userId = claims.getSubject(); // "sub" 필드에서 userId 추출
        String account = claims.get("account", String.class);
        String role = claims.get("role", String.class); // "role" 필드에서 권한 추출

        // 사용자 정보를 요청 헤더에 추가
        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header("X-User-Id", userId)
                .header("X-User-Account", account)
                .header("X-Role", role)
                .header(AUTHORIZATION_HEADER, token) // Bearer 포함한 토큰 헤더에 추가
                .build();

        return exchange.mutate().request(request).build();
    }

    private Jws<Claims> getClaimsJws(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
