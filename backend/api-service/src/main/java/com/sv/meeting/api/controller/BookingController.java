package com.sv.meeting.api.controller;


import com.sv.meeting.api.dto.BookingRequestDto;
import com.sv.meeting.api.dto.BookingResponseDto;
import com.sv.meeting.api.security.JwtPrincipal;
import com.sv.meeting.api.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms/{roomId}/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> book(@PathVariable long roomId,
                                                   @Valid @RequestBody BookingRequestDto req,
                                                   @AuthenticationPrincipal JwtPrincipal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookMeetingRoom(roomId, principal.userId(), req));
    }
}
