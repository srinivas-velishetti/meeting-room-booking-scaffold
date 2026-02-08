package com.sv.meeting.api.repo;

import com.sv.meeting.api.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select case when count(*) > 0 then true else false end from bookings b where b.room_id=:roomId and :startUtc < b.end_utc and :endUtc > b.start_utc", nativeQuery = true)
    boolean existsOverlap(@Param("roomId") long roomId, @Param("startUtc") Instant startUtc, @Param("endUtc") Instant endUtc);
}
