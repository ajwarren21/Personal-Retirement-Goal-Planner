package com.skillstorm.dtos;

public record ResponseUserDto(
    Long id,
    String username,
    String email
) {}
