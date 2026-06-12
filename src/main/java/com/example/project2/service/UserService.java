package com.example.project2.service;

import com.example.project2.dto.UserResponse;
import com.example.project2.dto.UserUpdateRequest;
import com.example.project2.dto.ChangePasswordRequest;
import com.example.project2.dto.ForgotPasswordOtpRequest;
import com.example.project2.dto.ResetPasswordWithOtpRequest;
import com.example.project2.entity.User;
import com.example.project2.entity.OtpCode;
import com.example.project2.entity.enums.Role;
import com.example.project2.exception.NotFoundException;
import com.example.project2.repository.OtpCodeRepository;
import com.example.project2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpCodeRepository otpCodeRepository;
    private final EmailService emailService;

    public Page<UserResponse> getAllUsers(int page, int size, String keyword, String roleStr) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Role role = null;
        if (roleStr != null && !roleStr.isEmpty()) {
            try {
                role = Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                role = null;
            }
        }

        Page<User> userPage = userRepository.searchUsers(keyword, role, pageable);

        List<UserResponse> responses = userPage.getContent().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, userPage.getTotalElements());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng với ID: " + id));
        return mapToUserResponse(user);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getEnabled() != null) user.setEnabled(request.getEnabled());
        if (request.getAccountNonLocked() != null) user.setAccountNonLocked(request.getAccountNonLocked());

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        userRepository.delete(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        currentUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(currentUser);
    }

    @Transactional
    public void sendForgotPasswordOtp(ForgotPasswordOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản liên kết với email này: " + request.getEmail()));

        // Sinh OTP gồm 6 chữ số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Xóa các OTP cũ của email này trước khi thêm mới
        otpCodeRepository.deleteByEmail(request.getEmail());

        // Lưu OTP mới với thời gian hết hạn là 5 phút
        OtpCode otpCode = OtpCode.builder()
                .email(request.getEmail())
                .code(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();
        otpCodeRepository.save(otpCode);

        // Gửi email
        emailService.sendOtpEmail(request.getEmail(), otp);
    }

    @Transactional
    public void resetPasswordWithOtp(ResetPasswordWithOtpRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không trùng khớp");
        }

        OtpCode otpCode = otpCodeRepository.findTopByEmailAndCodeOrderByExpiryTimeDesc(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new IllegalArgumentException("Mã OTP không hợp lệ hoặc đã hết hạn"));

        if (otpCode.isExpired()) {
            otpCodeRepository.delete(otpCode);
            throw new IllegalArgumentException("Mã OTP đã hết hạn sử dụng");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng liên kết với email này: " + request.getEmail()));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Xóa mã OTP sau khi sử dụng thành công
        otpCodeRepository.delete(otpCode);
    }
}