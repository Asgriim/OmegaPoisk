package org.omega.authservice.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SecurityConfig {

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.header:Refresh-Token}")
    private String refreshTokenHeader;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.token-type:JWT}")
    private String tokenType;

    @Value("${security.jwt.token-issuer:interview-secure-api}")
    private String tokenIssuer;

    @Value("${security.jwt.token-audience:interview-secure-app}")
    private String tokenAudience;


    @Value("${security.auth.expire-time:86400000}")
    private Long authExpireTime;


}
