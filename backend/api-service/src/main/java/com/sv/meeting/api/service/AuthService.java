package com.sv.meeting.api.service;


import com.sv.meeting.api.dto.LoginRequest;
import com.sv.meeting.api.dto.LoginResponse;
import com.sv.meeting.api.dto.RegisterRequest;
import com.sv.meeting.api.entity.User;
import com.sv.meeting.api.exception.BadRequestException;
import com.sv.meeting.api.repo.UserRepository;
import com.sv.meeting.api.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email().toLowerCase()))
            throw new BadRequestException("Email already registered");
        userRepository.save(User.builder()
                .name(req.name())
                .email(req.email().toLowerCase())
                .passwordHash(encoder.encode(req.password()))
                .roles("USER")
                .build());
    }

    public LoginResponse login(LoginRequest req, long expirySeconds) {
        User u = userRepository.findByEmail(req.email().toLowerCase()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!encoder.matches(req.password(), u.getPasswordHash())) throw new BadRequestException("Invalid credentials");
        String token = jwtUtil.generateToken(u.getId(), u.getEmail(), u.getRoles());
        return new LoginResponse(token, "Bearer", expirySeconds);
    }
}
