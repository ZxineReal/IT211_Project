package com.example.project2.service;

import com.example.project2.dto.BookingRequest;
import com.example.project2.dto.BookingResponse;
import com.example.project2.entity.Booking;
import com.example.project2.entity.Court;
import com.example.project2.entity.TimeSlot;
import com.example.project2.entity.User;
import com.example.project2.entity.enums.BookingStatus;
import com.example.project2.repository.BookingRepository;
import com.example.project2.repository.CourtRepository;
import com.example.project2.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserService userService;

    public BookingResponse createBooking(BookingRequest request) {
        User currentUser = userService.getCurrentUser();

        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new RuntimeException("Court not found"));

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new RuntimeException("Time slot not found"));

        boolean exists = bookingRepository.existsByCourtIdAndBookingDateAndTimeSlotIdAndStatusIn(
                request.getCourtId(), request.getBookingDate(), request.getTimeSlotId(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED));

        if (exists) {
            throw new RuntimeException("Khung giờ này đã được đặt");
        }

        Booking booking = Booking.builder()
                .user(currentUser)
                .court(court)
                .timeSlot(timeSlot)
                .bookingDate(request.getBookingDate())
                .status(BookingStatus.PENDING)
                .totalPrice(timeSlot.getPrice())
                .note(request.getNote())
                .createdAt(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        return mapToResponse(saved);
    }

    public Page<BookingResponse> getMyBookings(int page, int size) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Booking> bookings = bookingRepository.findByUserId(currentUser.getId(), pageable);

        List<BookingResponse> responses = bookings.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return new PageImpl<>(responses, pageable, bookings.getTotalElements());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .courtName(booking.getCourt().getName())
                .bookingDate(booking.getBookingDate())
                .timeSlot(booking.getTimeSlot().getStartTime() + " - " + booking.getTimeSlot().getEndTime())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .note(booking.getNote())
                .build();
    }
}