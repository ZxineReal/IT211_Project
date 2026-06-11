package com.example.project2.entity;

import com.example.project2.entity.enums.CourtStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;


@Entity
@Table(name = "time_slots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private BigDecimal price;               // Giá theo khung giờ (có thể khác basePrice)

    @Enumerated(EnumType.STRING)
    private CourtStatus status = CourtStatus.ACTIVE;
}