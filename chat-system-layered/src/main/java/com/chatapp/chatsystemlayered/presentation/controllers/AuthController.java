package com.chatapp.chatsystemlayered.presentation.controllers;

import com.chatapp.chatsystemlayered.application.services.AuthenticationService;
import com.chatapp.chatsystemlayered.presentation.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String token = authenticationService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
