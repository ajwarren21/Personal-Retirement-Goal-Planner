package com.skillstorm.dtos;

// package com.skillstorm.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
// import java.util.List;

import com.skillstorm.enums.ContributionCategory;
// import com.skillstorm.models.Contribution;
// import com.skillstorm.models.FundingSource;
// import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.User;

import jakarta.validation.constraints.DecimalMin;
// import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record ContributionDto(
    @NotNull(message = "User is required") User user,

    @NotNull(message = "Retirement Goal is required") long RetirementGoalId,

    @NotNull(message = "Funding Source is required") long fundingSourceId,
    
    @NotNull @DecimalMin(value = "0.01",
                message = "Total amount must be greater than zero")BigDecimal amount,
    
    @NotNull(message = "Date is required") @PastOrPresent(message = "Date cannot be in the future") LocalDate contributionDate,
    
    @NotNull(message = "Category is required") ContributionCategory category,

    String notes
) {}
