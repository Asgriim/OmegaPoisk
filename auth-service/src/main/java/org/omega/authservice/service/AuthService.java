package org.omega.authservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.authservice.dto.UserDTO;
import org.omega.authservice.entity.RoleEntity;
import org.omega.authservice.entity.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final RoleService roleService;
    private final UserService userService;

    public String logIn(UserDTO userDTO) {
        var user = userDetailsService.loadUserByUsername(userDTO.getLogin());
        if (!passwordEncoder.matches(userDTO.getPass(), user.getPassword())) {
            throw new BadCredentialsException("Invalid user or password");
        }
        return jwtService.generateToken(user);
    }

    public void registerUser(UserDTO userDTO) {
        RoleEntity roleEntity = roleService.getByRoleName(userDTO.getRole());
        User user = userDTO.toUser();
        user.setPassword(passwordEncoder.encode(userDTO.getPass()));
        user.setRoleId(roleEntity.getId());
        userService.createUser(user);
    }
}
