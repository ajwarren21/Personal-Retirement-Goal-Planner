package com.skillstorm.services;

// import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.mappers.RetirementGoalMapper;
import com.skillstorm.models.RetirementGoal;
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
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    public ResponseRetirementGoalDto getById(long id) {
        RetirementGoal r = repo.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        return mapper.toDto(r);
    }

    public ResponseRetirementGoalDto create(RetirementGoalDto dto) {
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    public ResponseRetirementGoalDto update(long id, RetirementGoalDto dto) {
        RetirementGoal r = repo.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        mapper.updateEntityFromDto(dto, r);

        repo.save(r);
        return mapper.toDto(r);
    }

    public void delete(long id) {
        RetirementGoal r = repo.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        repo.delete(r);
    }


}
