package com.iamo.springjwt.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtRefreshTokenStore {

    private final RedisTemplate<String, String> redisTemplate;

    public JwtRefreshTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void save(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofSeconds(6000));
    }

    public boolean isValid(String username, String refreshToken) {

        Object cachedUser = redisTemplate.opsForValue().get(username);

        if (cachedUser != null) {
            return cachedUser.equals(refreshToken);
        }
        return false;
    }

    public void remove(String username) {
        redisTemplate.delete(username);
    }
}

