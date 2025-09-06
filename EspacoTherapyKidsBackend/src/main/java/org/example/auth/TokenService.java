package org.example.auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {
    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    public String issueToken(String username) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, username);
        return token;
    }
    public boolean isValid(String token) {
        return token != null && tokens.containsKey(token);
    }
    public void revoke(String token) {
        if (token != null) tokens.remove(token);
    }
}
