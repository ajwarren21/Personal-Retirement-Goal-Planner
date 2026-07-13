package com.skillstorm.services;

// import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.mappers.RetirementGoalMapper;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.User;
import com.skillstorm.repositories.RetirementGoalRepository;
import com.skillstorm.repositories.UserRepository;

@Service
public class RetirementGoalService {
    
    private final RetirementGoalRepository repo;
    private final RetirementGoalMapper mapper;
    private final UserRepository userRepository;

    public RetirementGoalService(RetirementGoalRepository repo, RetirementGoalMapper mapper, UserRepository userRepository) {
        this.repo = repo;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public Iterable<ResponseRetirementGoalDto> getGoalsByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Iterable<ResponseRetirementGoalDto> temp = repo.findByUserId(user.getId()).stream().map(mapper::toDto).toList();
        temp.forEach(goal -> System.out.println("Retirement Goal ID: " + goal.id())); 
        return temp;
        // return repo.findByUserId(user.getId()).stream().map(mapper::toDto).toList();
    }

    // public Iterable<ResponseRetirementGoalDto> getAll() {
    //     return repo.findAll().stream().map(mapper::toDto).toList();
    // }

    public ResponseRetirementGoalDto getById(long id) {
        RetirementGoal r = repo.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        return mapper.toDto(r);
    }

    /**
     * Get a goal by ID with user ownership verification.
     */
    public ResponseRetirementGoalDto getByIdForUser(long id, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        RetirementGoal r = repo.findById(id)
            .orElseThrow(() -> new GoalNotFoundException(id));
        
        // Verify the goal belongs to this user
        if (!r.getUser().getId().equals(user.getId())) {
            throw new GoalNotFoundException(id);
        }
        
        return mapper.toDto(r);
    }


    public ResponseRetirementGoalDto createGoalForUser(RetirementGoalDto goalDto, String username) {
        // Find the user object in the DB
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        RetirementGoal goal = mapper.toEntity(goalDto);
        // Link the funding source to this specific user profile
        goal.setUser(user);
        
        // Persist to database
        return mapper.toDto(repo.save(goal));
    }

    // public ResponseRetirementGoalDto create(RetirementGoalDto dto) {
    //     return mapper.toDto(repo.save(mapper.toEntity(dto)));
    // }

    public ResponseRetirementGoalDto update(long id, RetirementGoalDto dto) {
        RetirementGoal r = repo.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        mapper.updateEntityFromDto(dto, r);

        repo.save(r);
        return mapper.toDto(r);
    }

    /**
     * Update a goal with user ownership verification.
     */
    public ResponseRetirementGoalDto updateForUser(long id, RetirementGoalDto dto, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        RetirementGoal r = repo.findById(id)
            .orElseThrow(() -> new GoalNotFoundException(id));
        
        if (!r.getUser().getId().equals(user.getId())) {
            throw new GoalNotFoundException(id);
        }
        
        mapper.updateEntityFromDto(dto, r);
        repo.save(r);
        return mapper.toDto(r);
    }

    public void delete(long id) {
        RetirementGoal r = repo.findById(id).orElseThrow(() -> new GoalNotFoundException(id));
        repo.delete(r);
    }

    /**
     * Delete a goal with user ownership verification.
     */
    public void deleteForUser(long id, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        RetirementGoal r = repo.findById(id)
            .orElseThrow(() -> new GoalNotFoundException(id));
        
        if (!r.getUser().getId().equals(user.getId())) {
            throw new GoalNotFoundException(id);
        }
        
        repo.delete(r);
    }


}
