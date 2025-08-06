package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.dto.LoginRequest;
import org.example.factory.ConnectionFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")  // ajuste à porta do seu front
public class AuthController {

    private static final int MAX_ATTEMPTS = 3;
    private static final String ATTR_ATTEMPTS = "loginAttempts";
    private static final String ATTR_AUTH     = "authenticated";

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest req,
            HttpSession session
    ) {
        Integer attempts = (Integer) session.getAttribute(ATTR_ATTEMPTS);
        if (attempts == null) attempts = 0;
        if (attempts >= MAX_ATTEMPTS) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Número máximo de tentativas excedido");
        }

        try (Connection conn = ConnectionFactory.getConnection(
           req.getUsername(),
           req.getPassword()
               )) {
            session.removeAttribute(ATTR_ATTEMPTS);
            session.setAttribute(ATTR_AUTH, true);
            return ResponseEntity.ok("Login bem-sucedido");
        } catch (SQLException ex) {
            attempts++;
            session.setAttribute(ATTR_ATTEMPTS, attempts);
            int rest = MAX_ATTEMPTS - attempts;
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciais inválidas. Restam: " + rest);
        }
    }
}
