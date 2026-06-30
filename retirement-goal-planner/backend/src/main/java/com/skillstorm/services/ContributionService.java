package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ContributionDto;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.exceptions.ContributionNotFoundException;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.ContributionMapper;
import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.repositories.ContributionRepository;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.RetirementGoalRepository;

@Service
public class ContributionService {

    private final ContributionRepository repo;
    private final ContributionMapper mapper;
    private final RetirementGoalRepository retireRepo;
    private final FundingSourceRepository fundingRepo;

    public ContributionService(ContributionRepository repo, ContributionMapper mapper, RetirementGoalRepository retireRepo, FundingSourceRepository fundingRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.retireRepo = retireRepo;
        this.fundingRepo = fundingRepo;
    }


    public Iterable<ResponseContributionDto> getAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    public ResponseContributionDto getById(long id) {
        Contribution c = repo.findById(id).orElseThrow(() -> new ContributionNotFoundException(id));
        return mapper.toDto(c);
    }

    public ResponseContributionDto update(long id, ContributionDto dto) {
        Contribution c = repo.findById(id).orElseThrow(() -> new ContributionNotFoundException(id));
 
        mapper.updateEntityFromDto(dto, c);
 
        repo.save(c);
        return mapper.toDto(c);
    }

    public ResponseContributionDto create(ContributionDto dto) {
        // need to find the referenced entitites first
        RetirementGoal goal = retireRepo.findById(dto.RetirementGoalId()).orElseThrow(() -> new GoalNotFoundException(dto.RetirementGoalId()));
        FundingSource fundingSource = fundingRepo.findById(dto.fundingSourceId()).orElseThrow(() -> new SourceNotFoundException(dto.fundingSourceId()));
 
        Contribution c = mapper.toEntity(dto);
 
        c.setGoal(goal);
        c.setFundingSource(fundingSource);
        return mapper.toDto(repo.save(c));
    }

    public void delete(long id) {
        Contribution c = repo.findById(id).orElseThrow(() -> new ContributionNotFoundException(id));
        repo.delete(c);
    }
    

}
