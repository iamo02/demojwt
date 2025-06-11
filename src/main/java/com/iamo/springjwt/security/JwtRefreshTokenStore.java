package com.iamo.springjwt.security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtRefreshTokenStore {
    private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();

    public void save(String username, String refreshToken) {
        refreshTokens.put(username, refreshToken);
    }

    public boolean isValid(String username, String refreshToken) {
        return refreshToken.equals(refreshTokens.get(username));
    }

    public void remove(String username) {
        refreshTokens.remove(username);
    }
}

