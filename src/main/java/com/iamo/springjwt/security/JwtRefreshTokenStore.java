package com.iamo.springjwt.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtRefreshTokenStore {

    private static final String PREFIX = "refresh:";
    private final RedisTemplate<String, String> redisTemplate;

    public JwtRefreshTokenStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void save(String username, String refreshToken) {

        Object cachedUser = redisTemplate.opsForValue().get(PREFIX+username);
        if (cachedUser != null) {
            remove(username);
        }
        redisTemplate.opsForValue().set(PREFIX+username, refreshToken, Duration.ofMinutes(10));
    }

    public boolean isValid(String username, String refreshToken) {

        Object cachedUser = redisTemplate.opsForValue().get(PREFIX+username);

        if (cachedUser != null) {
            return cachedUser.equals(refreshToken);
        }
        return false;
    }

    public void remove(String username) {
        redisTemplate.delete(PREFIX+username);
    }

    public void refreshToken(String username,String refreshToken) {
        remove(username);
        save(username,refreshToken);

    }

}

