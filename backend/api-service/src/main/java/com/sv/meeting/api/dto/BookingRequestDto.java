package com.sv.meeting.api.dto;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
public record BookingRequestDto(@NotNull OffsetDateTime startTime, @NotNull OffsetDateTime endTime, String title) {}
