package org.omega.omegapoisk.config;

import org.omega.omegapoisk.security.AuthFilter;
import org.omega.omegapoisk.security.RestAuthenticationEntryPoint;
import org.omega.omegapoisk.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConf {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserService userService;

    private RestAuthenticationEntryPoint entryPoint;

    @Bean
    public AuthFilter filter(){
        return new AuthFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.disable())
                .csrf(cs -> cs.disable())
                .addFilterBefore(filter(), AuthorizationFilter.class)
                .addFilterBefore(filter(), AuthorizationFilter.class)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("**").permitAll()
//                        auth -> auth.requestMatchers("/auth/**").permitAll()
                )
//                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        ;

        return http.build();
    }



    @Bean
    public WebMvcConfigurer corsConfigurer(){

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
                        .allowedHeaders("*")
                        .allowedOrigins(null,"null")
                        .allowCredentials(true);
            }
        };
    }
}
