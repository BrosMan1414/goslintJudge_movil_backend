package com.goslint.goslint.auth;

import com.goslint.goslint.team.TeamService;
import com.goslint.goslint.team.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final TeamService teamService;

    public AuthController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        boolean ok = teamService.login(request);
        if (ok) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).body("Nombre o contraseña incorrectos");
    }
}
