package com.skillstorm.services;

import com.skillstorm.dtos.ContributionDto;
import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.mappers.ContributionMapper;
import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.repositories.ContributionRepository;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.RetirementGoalRepository;

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
        Contribution c = repo.findById(id).orElseThrow();
        return mapper.toDto(c);
    }

    public ResponseContributionDto update(ContributionDto dto) {
        Contribution c = repo.findById(dto.id()).orElseThrow();
 
        mapper.updateEntityFromDto(dto, c);
 
        repo.save(c);
        return mapper.toDto(c);
    }

    public ResponseContributionDto create(ContributionDto dto) {
        // need to find the referenced entitites first
        RetirementGoal goal = retireRepo.findById(dto.goal().getId()).orElseThrow();
        FundingSource fundingSource = fundingRepo.findById(dto.fundingSource().getId()).orElseThrow();
 
        Contribution c = mapper.toEntity(dto);
 
        c.setGoal(goal);
        c.setFundingSource(fundingSource);
        return mapper.toDto(repo.save(c));
    }

    public void delete(long id) {
        Contribution c = repo.findById(id).orElseThrow();
        repo.delete(c);
    }
    

}
