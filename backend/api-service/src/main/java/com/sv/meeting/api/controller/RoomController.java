package com.sv.meeting.api.controller;

import com.sv.meeting.api.entity.Room;
import com.sv.meeting.api.service.RoomService;
import jakarta.annotation.PostConstruct;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostConstruct
    void seed() {
        roomService.seedIfEmpty();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> available(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end,
            @RequestParam(required = false) Integer capacity) {
        return ResponseEntity.ok(roomService.findAvailableRooms(start.toInstant(), end.toInstant(), capacity));
    }
}
