package org.example.portfolioshowcase.service;

import org.example.portfolioshowcase.common.JwtUtil;
import org.example.portfolioshowcase.dto.request.LoginRequest;
import org.example.portfolioshowcase.dto.response.LoginResponse;
import org.example.portfolioshowcase.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    public LoginResponse login(LoginRequest request) {
        if (request == null
                || !adminUsername.equals(request.username())
                || !adminPassword.equals(request.password())) {
            throw new BusinessException(40100, "用户名或密码错误");
        }
        return new LoginResponse(jwtUtil.generateToken(adminUsername), adminUsername);
    }
}
