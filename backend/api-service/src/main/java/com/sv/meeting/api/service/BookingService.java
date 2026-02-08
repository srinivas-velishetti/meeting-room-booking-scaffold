package com.sv.meeting.api.service;

import com.sv.meeting.api.dto.BookingRequestDto;
import com.sv.meeting.api.dto.BookingResponseDto;
import com.sv.meeting.api.entity.Booking;
import com.sv.meeting.api.exception.BadRequestException;
import com.sv.meeting.api.exception.ConflictException;
import com.sv.meeting.api.exception.NotFoundException;
import com.sv.meeting.api.repo.BookingRepository;
import com.sv.meeting.api.repo.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
//    private final BookingEventPublisher eventPublisher;

    BookingService(RoomRepository roomRepository, BookingRepository bookingRepository) {

        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingResponseDto bookMeetingRoom(long roomId, long userId, BookingRequestDto bookingRequestDto) {

        //serialize booking per room
        roomRepository.lockRoom(String.valueOf(roomId)).orElseThrow(() -> new NotFoundException("Room not found : " + roomId));

        //check if it's overlapping with existing booking - adjacent allowed
        boolean isOverlap = bookingRepository.existsOverlap(roomId, bookingRequestDto.startTime().toInstant(), bookingRequestDto.endTime().toInstant());
        if (isOverlap) {

//            eventPublisher.publish(roomId, userId, req, "FAILED_CONFLICT", "Overlapping booking", null); // TODO  integrate
            throw new ConflictException("Room already booked for the requested time window");
        }

        Booking booking = Booking.createBooking(roomId, userId, bookingRequestDto.startTime().toInstant(), bookingRequestDto.endTime().toInstant(), bookingRequestDto.title());
        bookingRepository.save(booking);
//        eventPublisher.publish(roomId, userId, req, "SUCCESS", null, booking.getId()); //TODO
        return new BookingResponseDto(booking.getId(), booking.getStatus());

    }

    private void validate(BookingRequestDto bookingRequestDto) {
        if (bookingRequestDto.startTime()== null || bookingRequestDto.endTime() == null) throw new BadRequestException("start/end required");
        if (!bookingRequestDto.startTime().isBefore(bookingRequestDto.endTime())) throw new BadRequestException("start must be before end");
    }
}
