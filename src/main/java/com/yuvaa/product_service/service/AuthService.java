package com.yuvaa.product_service.service;

import com.yuvaa.product_service.dto.AuthRequest;
import com.yuvaa.product_service.dto.AuthResponse;
import com.yuvaa.product_service.dto.RefreshTokenRequest;

public interface AuthService {
    AuthResponse register(AuthRequest request);
    AuthResponse login(AuthRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}