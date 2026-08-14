package com.ann.chat.identityservice.controller;

import com.ann.chat.identityservice.domain.User;
import com.ann.chat.identityservice.infrastructure.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository users;

    public AuthController(UserRepository users) {
        this.users = users;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User login) {

        return users.findById(login.getId())
                .filter(u -> u.getPassword().equals(login.getPassword()))
                .map(u -> ResponseEntity.ok("token-" + u.getId()))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
}
