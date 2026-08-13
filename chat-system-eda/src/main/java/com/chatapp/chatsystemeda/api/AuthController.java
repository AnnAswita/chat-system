package com.chatapp.chatsystemeda.api;

import com.chatapp.chatsystemeda.commandbus.CommandBus;
import com.chatapp.chatsystemeda.commands.AuthenticateUserCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CommandBus commandBus;

    public AuthController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticateUserCommand cmd) {

        // dispatch is now async-safe and returns void
        commandBus.dispatch(cmd);

        // read the token that the handler stored inside the command
        return ResponseEntity.ok(cmd.getGeneratedToken());
    }

}
