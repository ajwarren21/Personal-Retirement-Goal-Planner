package com.skillstorm.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.skillstorm.models.Contribution;
import com.skillstorm.models.User;



public record RetirementGoalDto(
    User user,
    String name,
    Integer targetRetirementAge,
    BigDecimal targetAmount,
    List<Contribution> contributions,
    String notes
) {}
