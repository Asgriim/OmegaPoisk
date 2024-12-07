package org.omega.contentservice.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            System.out.println("Token service: " + authentication.getCredentials());
            return (String) authentication.getCredentials(); // Предположительно, токен находится в `credentials`
        }
        return null;
    }
}