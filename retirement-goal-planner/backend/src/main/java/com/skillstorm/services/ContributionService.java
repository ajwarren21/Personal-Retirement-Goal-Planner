package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ContributionDto;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.exceptions.ContributionNotFoundException;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.ContributionMapper;
import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.User;
import com.skillstorm.repositories.ContributionRepository;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.RetirementGoalRepository;
import com.skillstorm.repositories.UserRepository;

@Service
public class ContributionService {

    private final ContributionRepository repo;
    private final ContributionMapper mapper;
    private final RetirementGoalRepository retireRepo;
    private final FundingSourceRepository fundingRepo;
    private final UserRepository userRepository;

    public ContributionService(ContributionRepository repo, ContributionMapper mapper, RetirementGoalRepository retireRepo, 
        FundingSourceRepository fundingRepo, UserRepository userRepository) {
        this.repo = repo;
        this.mapper = mapper;
        this.retireRepo = retireRepo;
        this.fundingRepo = fundingRepo;
        this.userRepository = userRepository;
    }


    // public Iterable<ResponseContributionDto> getAll() {
    //     return repo.findAll().stream().map(mapper::toDto).toList();
    // }

    public Iterable<ResponseContributionDto> getContributionsByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return repo.findByUserId(user.getId()).stream().map(mapper::toDto).toList();
    }

    public ResponseContributionDto getById(long id) {
        Contribution c = repo.findById(id).orElseThrow(() -> new ContributionNotFoundException(id));
        return mapper.toDto(c);
    }

    /**
     * Get a contribution by ID with user ownership verification.
     */
    public ResponseContributionDto getByIdForUser(long id, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Contribution c = repo.findById(id)
            .orElseThrow(() -> new ContributionNotFoundException(id));
        
        if (!c.getUser().getId().equals(user.getId())) {
            throw new ContributionNotFoundException(id);
        }
        
        return mapper.toDto(c);
    }

    // public ResponseContributionDto update(long id, ContributionDto dto) {
    //     Contribution c = repo.findById(id).orElseThrow(() -> new ContributionNotFoundException(id));
 
    //     mapper.updateEntityFromDto(dto, c);
 
    //     repo.save(c);
    //     return mapper.toDto(c);
    // }

    /**
     * Update a contribution with user ownership verification.
     */
    public ResponseContributionDto updateForUser(long id, ContributionDto dto, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Contribution c = repo.findById(id)
            .orElseThrow(() -> new ContributionNotFoundException(id));
        
        if (!c.getUser().getId().equals(user.getId())) {
            throw new ContributionNotFoundException(id);
        }

        mapper.updateEntityFromDto(dto, c);
        repo.save(c);
        return mapper.toDto(c);
    }

    public ResponseContributionDto createContributionForUser(ContributionDto dto, String username) {

        // Find the user object in the DB
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Link the funding source to this specific user profile
        

        // need to find the referenced entitites first
        RetirementGoal goal = retireRepo.findById(dto.retirementGoalId()).orElseThrow(() -> new GoalNotFoundException(dto.retirementGoalId()));
        FundingSource fundingSource = fundingRepo.findById(dto.fundingSourceId()).orElseThrow(() -> new SourceNotFoundException(dto.fundingSourceId()));
 

        if (goal.getUser().getId() != user.getId()) {
            throw new GoalNotFoundException(dto.retirementGoalId());
        }
        if (fundingSource.getUser().getId() != user.getId()) {
            throw new SourceNotFoundException(dto.fundingSourceId());
        }

        Contribution c = mapper.toEntity(dto);

        c.setUser(user);
 
        c.setGoal(goal);
        c.setFundingSource(fundingSource);
        return mapper.toDto(repo.save(c));
        // So because of how the model is setup, this should all attach the c to the list of cs on a goal
    }

    public void delete(long id) {
        Contribution c = repo.findById(id).orElseThrow(() -> new ContributionNotFoundException(id));
        repo.deleteById(id);
    }

    /**
     * Delete a contribution with user ownership verification.
     */
    public void deleteForUser(long id, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Contribution c = repo.findById(id)
            .orElseThrow(() -> new ContributionNotFoundException(id));
        
        if (!c.getUser().getId().equals(user.getId())) {
            throw new ContributionNotFoundException(id);
        }
        
        repo.deleteById(id);
    }
    

}
