package com.sv.meeting.api.repo;

import com.sv.meeting.api.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // TODO explore
    @Query("select r from Room r where r.id =:roomId")
    Optional<Room> lockRoom(@Param("roomId") String roomId);
}
