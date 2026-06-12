package com.example.project2.controller;

import com.example.project2.dto.ApiResponse;
import com.example.project2.dto.ForgotPasswordOtpRequest;
import com.example.project2.dto.LoginResponse;
import com.example.project2.dto.ResetPasswordWithOtpRequest;
import com.example.project2.dto.TokenRefreshRequest;
import com.example.project2.dto.UserLoginRequest;
import com.example.project2.dto.UserRegisterRequest;
import com.example.project2.dto.UserResponse;
import com.example.project2.service.AuthService;
import com.example.project2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegisterRequest request) {

        UserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Đăng ký tài khoản thành công")
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody UserLoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Đăng nhập thành công")
                .data(response)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @Valid @RequestBody TokenRefreshRequest request) {

        LoginResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Lấy token mới thành công")
                .data(response)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader) {

        authService.logout(authHeader);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Đăng xuất thành công")
                .build());
    }

    @PostMapping("/forgot-password/otp")
    public ResponseEntity<ApiResponse<Void>> sendForgotPasswordOtp(
            @Valid @RequestBody ForgotPasswordOtpRequest request) {

        userService.sendForgotPasswordOtp(request);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Mã OTP đã được gửi đến email của bạn. Vui lòng kiểm tra hộp thư.")
                .build());
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPasswordWithOtp(
            @Valid @RequestBody ResetPasswordWithOtpRequest request) {

        userService.resetPasswordWithOtp(request);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Đặt lại mật khẩu thành công. Vui lòng đăng nhập bằng mật khẩu mới.")
                .build());
    }
}
