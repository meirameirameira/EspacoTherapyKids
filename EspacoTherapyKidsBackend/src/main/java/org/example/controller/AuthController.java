package org.example.controller;

import org.example.auth.TokenService;
import org.example.dto.LoginRequest;
import org.example.web.ApiError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final TokenService tokens;

    @Value("${app.auth.username:admin}")
    private String confUser;

    @Value("${app.auth.password:123}")
    private String confPass;

    public AuthController(TokenService tokens) {
        this.tokens = tokens;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String user = req != null ? req.getUsername() : null;
        String pass = req != null ? req.getPassword() : null;

        if (!StringUtils.hasText(user) || !StringUtils.hasText(pass)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiError(OffsetDateTime.now(), "/api/auth/login", 400, "Bad Request",
                            "CREDENCIAIS_INVALIDAS", "Usuário e senha são obrigatórios."));
        }

        if (!confUser.equals(user) || !confPass.equals(pass)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError(OffsetDateTime.now(), "/api/auth/login", 401, "Unauthorized",
                            "NAO_AUTORIZADO", "Usuário ou senha inválidos."));
        }

        String token = tokens.issueToken(user);
        return ResponseEntity.ok(Map.of("tokenType", "Bearer", "accessToken", token));
    }
}
