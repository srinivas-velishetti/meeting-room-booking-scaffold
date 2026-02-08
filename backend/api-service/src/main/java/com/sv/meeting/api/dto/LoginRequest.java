package com.sv.meeting.api.dto;
import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
