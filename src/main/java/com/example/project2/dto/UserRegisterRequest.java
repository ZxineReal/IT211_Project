package com.example.project2.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 4, max = 50)
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private String phoneNumber;
}
