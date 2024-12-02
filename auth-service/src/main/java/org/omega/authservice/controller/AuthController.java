package org.omega.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.omega.authservice.dto.UserDTO;
import org.omega.authservice.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated UserDTO request) {
        String jwt = authService.logIn(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/test")
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/test2")
    public ResponseEntity<Void> test2() {
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/test3")
    public ResponseEntity<Void> test3() {
        return ResponseEntity.ok().build();
    }
}
