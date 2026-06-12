package com.example.project2.aspect;

import com.example.project2.dto.BookingRequest;
import com.example.project2.dto.BookingResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @AfterReturning(
            pointcut = "execution(* com.example.project2.service.BookingService.createBooking(..))",
            returning = "result"
    )
    public void logBookingSuccess(JoinPoint joinPoint, Object result) {
        if (result instanceof BookingResponse response) {
            String username = SecurityContextHolder.getContext().getAuthentication() != null
                    ? SecurityContextHolder.getContext().getAuthentication().getName()
                    : "Anonymous";

            log.info("[AUDIT - SUCCESS] Khách hàng {} đặt thành công {} vào ngày {}, Khung giờ {}",
                    username,
                    response.getCourtName(),
                    response.getBookingDate(),
                    response.getTimeSlot());
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.project2.service.BookingService.createBooking(..))",
            throwing = "ex"
    )
    public void logBookingFailure(JoinPoint joinPoint, Exception ex) {
        Object[] args = joinPoint.getArgs();
        String username = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "Anonymous";

        Long courtId = null;
        if (args != null && args.length > 0 && args[0] instanceof BookingRequest request) {
            courtId = request.getCourtId();
        }

        log.error("[AUDIT - FAILED] Khách hàng {} cố gắng đặt Sân số {} nhưng thất bại do {}",
                username,
                courtId != null ? courtId : "không xác định",
                ex.getMessage());
    }
}
