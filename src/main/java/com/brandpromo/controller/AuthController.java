package com.brandpromo.controller;

import com.brandpromo.dto.*;
import com.brandpromo.entity.User;
import com.brandpromo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/register")
    public ApiResponse<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        user.setPassword(null);  // don't return password
        return ApiResponse.ok("Registration successful", user);
    }

    @GetMapping("/me")
    public ApiResponse<User> me(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        user.setPassword(null);
        return ApiResponse.ok(user);
    }
}
