package com.example.project2.dto;

import com.example.project2.entity.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String fullName;
    private String phoneNumber;
    private Boolean enabled;
    private Boolean accountNonLocked;
    private Role role;
}