package org.omega.omegapoisk.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.UserDTO;
import org.omega.omegapoisk.entity.user.RoleEntity;
import org.omega.omegapoisk.entity.user.User;
import org.omega.omegapoisk.exception.InvaliUserOrPasswordException;
import org.omega.omegapoisk.security.JWT;
import org.omega.omegapoisk.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserService userService;
    private final JWT jwt;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        User user = (User) userService.loadUserByUsername(userDTO.getLogin());

        if (!encoder.matches(userDTO.getPass(),user.getPassword())) {
            throw new InvaliUserOrPasswordException();
        }

        String jwtS = jwt.generateToken(userDTO.getLogin());

        return ResponseEntity.status(202).body("token: " + jwtS);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO){
        User user = new User();

        user.setLogin(userDTO.getLogin());
        user.setPassword(encoder.encode(userDTO.getPass()));
        user.setEmail(userDTO.getEmail());
        user.setRole(new RoleEntity(0, userDTO.getRole()));

        userService.registerUser(user);

        return login(userDTO);
    }
}
