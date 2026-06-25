package com.skillstorm.dtos;

// package com.skillstorm.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
// import java.util.List;

import com.skillstorm.enums.ContributionCategory;
// import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.User;

// import jakarta.validation.constraints.DecimalMin;
// import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.PastOrPresent;

public record ContributionDto(
    User user,
    RetirementGoal goal,
    FundingSource fundingSource,
    BigDecimal amount,
    LocalDate contributionDate,
    ContributionCategory category
) {}
