package com.sv.meeting.api.service;

import com.sv.meeting.api.entity.Room;
import com.sv.meeting.api.exception.NotFoundException;
import com.sv.meeting.api.repo.BookingRepository;
import com.sv.meeting.api.repo.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Room> findAvailableRooms(Instant startUtc, Instant endUtc, Integer capacity) {
        List<Room> availableRooms = roomRepository.findAll().stream().filter(room -> Boolean.TRUE.equals(room.getActive()))
                .filter(room -> capacity == null || room.getCapacity() >= capacity)
                .toList();

        return availableRooms.stream()
                .filter(r1 -> !bookingRepository.existsOverlap(r1.getId(), startUtc, endUtc))
                .map(room -> Room.builder().id(room.getId()).name(room.getName()).building(room.getBuilding()).floor(room.getFloor()).capacity(room.getCapacity()).build())
                .collect(Collectors.toList());
    }

    public void seedIfEmpty() {
        if (roomRepository.count() == 0) {
            roomRepository.save(Room.builder().name("A-410").building("A").floor("4").capacity(8).equipmentJson("[]").active(true).build());
            roomRepository.save(Room.builder().name("A-411").building("A").floor("4").capacity(6).equipmentJson("[]").active(true).build());
            roomRepository.save(Room.builder().name("B-210").building("B").floor("2").capacity(12).equipmentJson("[]").active(true).build());
        }
    }

}



