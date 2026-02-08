package com.sv.meeting.api.security;

public record JwtPrincipal(long userId, String email) {}
