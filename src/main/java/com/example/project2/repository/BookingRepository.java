package com.example.project2.repository;

import com.example.project2.entity.Booking;
import com.example.project2.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    boolean existsByCourtIdAndBookingDateAndTimeSlotIdAndStatusIn(
            Long courtId, LocalDate bookingDate, Long timeSlotId, List<BookingStatus> statuses);

    @Query("SELECT b FROM Booking b WHERE b.court.id = :courtId AND b.bookingDate = :bookingDate")
    List<Booking> findByCourtAndDate(Long courtId, LocalDate bookingDate);
}