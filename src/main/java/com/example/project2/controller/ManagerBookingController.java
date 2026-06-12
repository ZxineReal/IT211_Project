package com.example.project2.controller;

import com.example.project2.dto.ApiResponse;
import com.example.project2.dto.BookingResponse;
import com.example.project2.entity.enums.BookingStatus;
import com.example.project2.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerBookingController {

    private final BookingService bookingService;

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> approveOrRejectBooking(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {

        BookingResponse response = bookingService.approveOrRejectBooking(id, status);
        String message = status == BookingStatus.CONFIRMED ? "Phê duyệt lịch đặt thành công" : "Từ chối lịch đặt thành công";
        return ResponseEntity.ok(ApiResponse.<BookingResponse>builder()
                .success(true)
                .message(message)
                .data(response)
                .build());
    }
}
