package com.sfa.gateway.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(-1)
public class AuthGlobalFilter implements GlobalFilter {

    @Value("${jwt.secret-key}")
    private String secretStr;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretStr.getBytes(StandardCharsets.UTF_8));
    }

    private final List<String> whiteList = new ArrayList<>();
    {
        whiteList.add("/api/user/login");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if (whiteList.contains(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return writeUnauthorized(exchange, "令牌缺失");
        }
        String token = authHeader.substring(7);

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            Long userId = claims.get("userId", Long.class);

            ServerHttpRequest newRequest = request.mutate()
                    .header("userId", String.valueOf(userId))
                    .build();
            return chain.filter(exchange.mutate().request(newRequest).build());

        } catch (ExpiredJwtException e) {
            return writeUnauthorized(exchange, "令牌已过期");
        } catch (Exception e) {
            e.printStackTrace();
            return writeUnauthorized(exchange, "令牌非法");
        }
    }

    private Mono<Void> writeUnauthorized(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}";
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(buffer));
    }
}
