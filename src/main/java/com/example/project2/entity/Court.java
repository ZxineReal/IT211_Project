package com.example.project2.entity;

import com.example.project2.entity.enums.CourtStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;                    // Sân 1, Sân VIP, ...

    private String description;

    @Column(nullable = false)
    private BigDecimal basePricePerHour;    // Giá cơ bản/giờ

    @Column(name = "image_url")
    private String imageUrl;                // Ảnh chính

    @Enumerated(EnumType.STRING)
    private CourtStatus status = CourtStatus.ACTIVE;

    private String location;                // Vị trí cụm sân

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;                   // Chủ sân / Quản lý

    // Một sân có nhiều ảnh
    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourtImage> images = new ArrayList<>();
}
