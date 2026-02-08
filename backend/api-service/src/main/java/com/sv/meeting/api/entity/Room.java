package com.sv.meeting.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 80)
    private String building;
    @Column(nullable = false, length = 20)
    private String floor;
    @Column(nullable = false)
    private Integer capacity;
    @Column(name = "equipment_json")
    private String equipmentJson;
    @Column(name = "is_active", nullable = false)
    private Boolean active;
}
