package com.ann.chat.identityservice.controller;

import com.ann.chat.identityservice.domain.Presence;
import com.ann.chat.identityservice.infrastructure.PresenceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presence")
public class PresenceController {

    private final PresenceRepository presence;

    public PresenceController(PresenceRepository presence) {
        this.presence = presence;
    }

    @PutMapping("/{userId}")
    public void update(@PathVariable String userId, @RequestBody Presence body) {
        body.setUserId(userId);
        presence.save(body);
    }

    @GetMapping
    public List<Presence> all() {
        return presence.findAll();
    }
}
