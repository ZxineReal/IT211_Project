package com.example.project2.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull
    private Long courtId;

    @NotNull
    private Long timeSlotId;

    @NotNull
    @FutureOrPresent
    private LocalDate bookingDate;

    private String note;
}
