package com.skillstorm.dtos;

import java.math.BigDecimal;
import java.util.List;

import com.skillstorm.models.Contribution;
import com.skillstorm.models.User;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;


public record RetirementGoalDto(
    long id,
    User user,
    String name,
    Integer targetRetirementAge,
    BigDecimal targetAmount,
    List<Contribution> contributions
    
) {}
