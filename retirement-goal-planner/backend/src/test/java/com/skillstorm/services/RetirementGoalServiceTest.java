package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.mappers.RetirementGoalMapper;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.repositories.RetirementGoalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class RetirementGoalServiceTest {
    @Mock
    private RetirementGoalRepository repo;

    @Mock
    private RetirementGoalMapper mapper;

    @InjectMocks
    private RetirementGoalService service;

    private RetirementGoal goal;
    private RetirementGoalDto requestDto;
    private ResponseRetirementGoalDto responseDto;

    @BeforeEach
    void setup() {
        goal = new RetirementGoal();
        goal.setId(1L);

        requestDto = mock(RetirementGoalDto.class);
        responseDto = mock(ResponseRetirementGoalDto.class);
    }


    @Test
    void getAllValid() {
        
    }

}
