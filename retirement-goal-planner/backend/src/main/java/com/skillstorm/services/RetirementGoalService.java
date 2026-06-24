package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.mappers.RetirementGoalMapper;
import com.skillstorm.repositories.RetirementGoalRepository;

@Service
public class RetirementGoalService {
    
    private final RetirementGoalRepository repo;
    private final RetirementGoalMapper mapper;

    public RetirementGoalService(RetirementGoalRepository repo, RetirementGoalMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Iterable<ResponseRetirementGoalDto> getAll() {

    }

    public ResponseRetirementGoalDto getById(long id) {

    }

    public ResponseRetirementGoalDto create(RetirementGoalDto dto) {

    }

    public ResponseRetirementGoalDto update(RetirementGoalDto dto) {

    }

    public void delete(long id) {

    }

    
}
