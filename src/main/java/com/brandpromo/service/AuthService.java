package com.brandpromo.service;

import com.brandpromo.dto.LoginRequest;
import com.brandpromo.dto.LoginResponse;
import com.brandpromo.dto.RegisterRequest;
import com.brandpromo.entity.User;
import com.brandpromo.mapper.UserMapper;
import com.brandpromo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("Account is disabled");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token, user.getUsername(), user.getRole());
    }

    public User register(RegisterRequest request) {
        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRole(request.getRole() != null ? request.getRole() : "USER");
        user.setStatus(1);

        userMapper.insert(user);
        return user;
    }

    public User getCurrentUser(String username) {
        return userMapper.findByUsername(username);
    }
}
