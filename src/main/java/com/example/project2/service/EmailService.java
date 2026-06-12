package com.example.project2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        log.info("[OTP GENERATED] Mã OTP của tài khoản {} là: {}", toEmail, otp);
        System.out.println("=================================================");
        System.out.println(">>> MÃ OTP ĐỂ ĐỔI MẬT KHẨU TÀI KHOẢN " + toEmail + " LÀ: " + otp);
        System.out.println("=================================================");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Mã OTP Khôi phục mật khẩu - Badminton App");
            message.setText("Chào bạn,\n\nMã OTP khôi phục mật khẩu của bạn là: " + otp + "\nMã này có thời hạn sử dụng là 5 phút.\n\nTrân trọng!");
            mailSender.send(message);
            log.info("Email gửi mã OTP đến {} thành công.", toEmail);
        } catch (Exception e) {
            log.error("Không thể gửi email đến {} (Lỗi: {}). OTP được in ra console.", toEmail, e.getMessage());
        }
    }
}
