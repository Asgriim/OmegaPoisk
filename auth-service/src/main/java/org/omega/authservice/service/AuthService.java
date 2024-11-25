package org.omega.authservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.authservice.dto.UserDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public String logIn(UserDTO userDTO) {
        var user = userDetailsService.loadUserByUsername(userDTO.getLogin());
        if (!passwordEncoder.matches(userDTO.getPass(), user.getPassword())) {
            throw new BadCredentialsException("Invalid user or password");
        }
        return jwtService.generateToken(user);
    }

}
