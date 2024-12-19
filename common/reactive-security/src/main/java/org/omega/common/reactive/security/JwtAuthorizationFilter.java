package org.omega.common.reactive.security;

import io.jsonwebtoken.Claims;
import org.omega.common.core.dto.UserDetailsDto;
import org.omega.common.core.security.JwtUtils;
import org.omega.common.core.security.SecurityHelper;
import org.omega.common.core.security.SecurityHelperClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter implements WebFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private final SecurityHelper securityHelper = new SecurityHelperClass();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange.getRequest().getHeaders());
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return Mono.justOrEmpty(parseClaims(token))
                .filter(this::isTokenValid)
                .map(this::createAuthenticationToken)
                .flatMap(authentication -> {
                    SecurityContext context = new SecurityContextImpl(authentication);
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }));
    }

    private String extractToken(HttpHeaders headers) {
        String token = headers.getFirst(HEADER);
        if (StringUtils.hasLength(token) && token.startsWith(PREFIX)) {
            return token.replace(PREFIX, "").trim();
        }
        return null;
    }

    private Claims parseClaims(String token) {
        return JwtUtils.getAccessClaims(token, securityHelper.getJwtValidationKey());
    }

    private boolean isTokenValid(Claims claims) {
        return claims != null && !isTokenExpired(claims) && isUserEnabled(claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private boolean isUserEnabled(Claims claims) {
        Boolean isEnabled = (Boolean) claims.get("enabled");
        return isEnabled != null && isEnabled;
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(Claims claims) {
        List<SimpleGrantedAuthority> authorities = Optional.ofNullable((List<?>) claims.get("roles"))
                .orElse(Collections.emptyList())
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());

        Long userId = ((Number) claims.get("id")).longValue();

        return new UsernamePasswordAuthenticationToken(
                new UserDetailsDto(userId, authorities, true, true, true, true),
                null,
                authorities
        );
    }
}

