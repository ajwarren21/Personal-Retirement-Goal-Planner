package com.skillstorm.dtos;

// package com.skillstorm.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
// import java.util.List;

import com.skillstorm.enums.ContributionCategory;
// import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
// import com.skillstorm.models.User;


public record ResponseContributionDto(
    long id,
    long retirementGoalId,
    String retirementGoalName,
    long fundingSourceId,
    String fundingSourceName,
    java.math.BigDecimal amount,
    java.time.LocalDate contributionDate,
    com.skillstorm.enums.ContributionCategory category,
    String notes
) {}
