package com.multitenant.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multitenant.auth.dto.SignupRequestBodyDTO;
import com.multitenant.auth.dto.TokensResponseDTO;
import com.multitenant.auth.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<TokensResponseDTO> signup(@RequestBody SignupRequestBodyDTO input, HttpServletResponse response) {
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
    

}
