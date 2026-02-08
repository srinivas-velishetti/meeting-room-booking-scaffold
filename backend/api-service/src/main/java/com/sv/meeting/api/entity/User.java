package com.sv.meeting.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO explore on this strategies
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 250)
    private String passwordHash;
    @Column(nullable = false, length = 200)
    private String roles;

}
