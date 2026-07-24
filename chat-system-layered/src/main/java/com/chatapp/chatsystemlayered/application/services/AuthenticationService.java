package com.chatapp.chatsystemlayered.application.services;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public String login(String username, String password) {
        // Minimal stub: in real system, validate credentials and issue JWT
        if (username == null || password == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return "dummy-jwt-token-for-" + username;
    }

    public boolean validateToken(String token) {
        // Stub: validate JWT
        return token != null && token.startsWith("dummy-jwt-token");
    }
}
