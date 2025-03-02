package com.example.spribe.repository;

import com.example.spribe.model.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    String EXISTS_BOOKING_QUERY = """
            SELECT COUNT(*) > 0
            FROM bookings b
            WHERE b.unit_id = :unitId
            AND b.status IN ('ACTIVE', 'PAID')
            AND b.date_to <= :dateTo AND b.date_from >= :dateFrom
            """;

    String EXPIRED_BOOKINGS_QUERY = """
            SELECT *
            FROM bookings
            WHERE status = 'ACTIVE' 
                AND created_at <= :thresholdTime
            """;

    @Query(value = EXISTS_BOOKING_QUERY, nativeQuery = true)
    boolean hasBookingForUnit(
            @Param("unitId") Long unitId,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo);

    @Query(value = EXPIRED_BOOKINGS_QUERY, nativeQuery = true)
    List<BookingEntity> findExpiredBookings(@Param("thresholdTime") LocalDateTime thresholdTime);
}
