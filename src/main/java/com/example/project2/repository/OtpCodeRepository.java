package com.example.project2.repository;

import com.example.project2.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findTopByEmailAndCodeOrderByExpiryTimeDesc(String email, String code);

    void deleteByEmail(String email);

    void deleteByExpiryTimeBefore(LocalDateTime now);
}
