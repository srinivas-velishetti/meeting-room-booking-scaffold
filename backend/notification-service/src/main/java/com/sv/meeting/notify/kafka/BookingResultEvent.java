package com.sv.meeting.notify.kafka;
import java.time.Instant;
public record BookingResultEvent(String eventId,String eventType,String status,Long bookingId,long roomId,long userId,
                                Instant startUtc,Instant endUtc,String reason,Instant createdAtUtc) {}
