package org.example.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.web.ApiError;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.OffsetDateTime;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokens;
    private final ObjectMapper mapper = new ObjectMapper();

    public AuthInterceptor(TokenService tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String path = req.getRequestURI();

        // liberar login e swagger
        if (path.startsWith("/api/auth/login") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui")) {
            return true;
        }

        String auth = req.getHeader("Authorization");
        String token = (auth != null && auth.toLowerCase().startsWith("bearer "))
                ? auth.substring(7).trim() : null;

        if (tokens.isValid(token)) return true;

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        ApiError body = new ApiError(
                OffsetDateTime.now(), req.getRequestURI(),
                401, "Unauthorized", "NAO_AUTENTICADO",
                "Forneça um token Bearer válido no cabeçalho Authorization."
        );
        res.getWriter().write(mapper.writeValueAsString(body));
        return false;
    }
}
