package com.iamo.springjwt.controller;


import com.iamo.springjwt.model.AuthResponse;
import com.iamo.springjwt.security.JwtRefreshTokenStore;
import com.iamo.springjwt.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final JwtRefreshTokenStore refreshTokenStore;

    public AuthController(JwtService jwtService, JwtRefreshTokenStore refreshTokenStore) {
        this.jwtService = jwtService;
        this.refreshTokenStore = refreshTokenStore;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestParam String username, HttpServletResponse response) {
        String accessToken = jwtService.generateAccessToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);
        refreshTokenStore.save(username, refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        String username = jwtService.extractUsername(refreshToken, true);

        if (!jwtService.isRefreshTokenValid(refreshToken, username)
                || !refreshTokenStore.isValid(username, refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        String newAccessToken = jwtService.generateAccessToken(username);
        return ResponseEntity.ok(new AuthResponse(newAccessToken, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response, @RequestParam String username) {
        refreshTokenStore.remove(username);

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());
        return ResponseEntity.noContent().build();
    }
}
