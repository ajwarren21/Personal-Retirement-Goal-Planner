package com.skillstorm.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FundingSourceDto(
    @NotBlank(message = "Funding source name is required")
    String name,

    @NotNull(message = "Funding source type is required")
    String sourceType,

    @NotBlank(message = "Institution is required")
    String institution,

    String notes

) {}
