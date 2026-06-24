package com.skillstorm.dtos;

public record ResponseFundingSourceDto(
    Long id,
    String name,
    String sourceType,
    String institution,
    String notes
) {}
