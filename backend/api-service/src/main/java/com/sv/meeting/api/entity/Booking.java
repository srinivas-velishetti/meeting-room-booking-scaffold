package com.sv.meeting.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "bookings", indexes = @Index(name = "idx_room_time", columnList = "room_id,start_utc,end_utc"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "start_utc", nullable = false)
    private Instant startUtc;
    @Column(name = "end_utc", nullable = false)
    private Instant endUtc;
    @Column(length = 150)
    private String title;
    @Column(nullable = false, length = 30)
    private String status;

//    public static Booking createBooking(long roomId, long userId, Instant startUtc, Instant endUtc, String title) {
//        return Booking.builder().roomId(roomId).userId(userId).startUtc(startUtc).endUtc(endUtc).title(title).status("CONFIRMED").build();
//    }

    public static Booking createBooking(long roomId, long userId, Instant startUtc, Instant endUtc, String title) {
        return Booking.builder().roomId(roomId).userId(userId).startUtc(startUtc).endUtc(endUtc).title(title).status("CONFIRMED").build();
    }


}
