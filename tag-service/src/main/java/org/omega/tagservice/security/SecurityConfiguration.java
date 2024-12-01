package org.omega.tagservice.security;

import org.omega.common.security.JwtAuthorizationFilter;
import org.omega.common.security.SecurityHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.annotation.web.configurers.SecurityContextConfigurer;
import org.springframework.security.config.annotation.web.configurers.ServletApiConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {
    private final SecurityHelper securityHelper;

    public SecurityConfiguration(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(new JwtAuthorizationFilter(
                        authentication -> authentication,
                        securityHelper
                ))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(SessionManagementConfigurer::disable)
                .headers(HeadersConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .servletApi(ServletApiConfigurer::disable)
                .securityContext(SecurityContextConfigurer::disable)
                .build();
    }
}

