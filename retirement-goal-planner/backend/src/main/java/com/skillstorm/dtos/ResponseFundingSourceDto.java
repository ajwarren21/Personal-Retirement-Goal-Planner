package com.skillstorm.dtos;

import java.util.List;

public record ResponseFundingSourceDto(
    Long id,
    String name,
    String sourceType,
    String institution,
    String notes,
    List<ResponseContributionDto> contributions
) {}
