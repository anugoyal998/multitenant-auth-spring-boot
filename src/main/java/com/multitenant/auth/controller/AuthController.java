package com.multitenant.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multitenant.auth.dto.LoginRequestBodyDTO;
import com.multitenant.auth.dto.SignupRequestBodyDTO;
import com.multitenant.auth.dto.TokensResponseDTO;
import com.multitenant.auth.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<TokensResponseDTO> signup(@Valid @RequestBody SignupRequestBodyDTO input, HttpServletResponse response) {
        TokensResponseDTO tokens = authService.signup(input);
        Cookie accessTokenCookie = new Cookie("accessToken", tokens.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
        Cookie isAccessTokenCookie = new Cookie("isAccessToken", "true");
        response.addCookie(isAccessTokenCookie);
        Cookie isRefreshTokenCookie = new Cookie("isRefreshToken", "true");
        response.addCookie(isRefreshTokenCookie);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponseDTO> login(@Valid @RequestBody LoginRequestBodyDTO input, HttpServletResponse response) {
        TokensResponseDTO tokens = authService.login(input);
        Cookie accessTokenCookie = new Cookie("accessToken", tokens.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(refreshTokenCookie);
        Cookie isAccessTokenCookie = new Cookie("isAccessToken", "true");
        response.addCookie(isAccessTokenCookie);
        Cookie isRefreshTokenCookie = new Cookie("isRefreshToken", "true");
        response.addCookie(isRefreshTokenCookie);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensResponseDTO> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies())
                            .filter(cookie -> "refreshToken".equals(cookie.getName()))
                            .findFirst()
                            .map(cookie -> cookie.getValue())
                            .orElseThrow(() -> new AuthenticationServiceException("Refresh Token not found inside the cookies"));
        TokensResponseDTO tokens = authService.refresh(refreshToken);
        Cookie accessTokenCookie = new Cookie("accessToken", tokens.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        // Cookie refreshTokenCookie = new Cookie("refreshToken", tokens.getRefreshToken());
        // refreshTokenCookie.setHttpOnly(true);
        // response.addCookie(refreshTokenCookie);
        Cookie isAccessTokenCookie = new Cookie("isAccessToken", "true");
        response.addCookie(isAccessTokenCookie);
        // Cookie isRefreshTokenCookie = new Cookie("isRefreshToken", "true");
        // response.addCookie(isRefreshTokenCookie);
        return ResponseEntity.ok(tokens);
    }
    
    
}
