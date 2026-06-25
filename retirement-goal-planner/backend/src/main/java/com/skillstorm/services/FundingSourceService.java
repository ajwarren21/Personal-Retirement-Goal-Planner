package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.FundingSourceMapper;
import com.skillstorm.repositories.FundingSourceRepository;

@Service
public class FundingSourceService {

    private final FundingSourceRepository repo;
    private final FundingSourceMapper mapper;

    public FundingSourceService(FundingSourceRepository repo, FundingSourceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Iterable<ResponseFundingSourceDto> getAllFundingSources() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    public ResponseFundingSourceDto getFundingSourceById(long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(() -> new SourceNotFoundException(id)));
    }

    public ResponseFundingSourceDto createFundingSource(FundingSourceDto dto) {
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    public ResponseFundingSourceDto updateFundingSource(long id, FundingSourceDto dto) {
        var entity = repo.findById(id).orElseThrow(() -> new SourceNotFoundException(id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    public void deleteFundingSource(long id) {
        repo.deleteById(id);
    }

}
