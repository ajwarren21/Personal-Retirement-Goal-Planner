package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.FundingSourceMapper;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.User;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.UserRepository;

@Service
public class FundingSourceService {

    private final FundingSourceRepository repo;
    private final UserRepository userRepository;
    private final FundingSourceMapper mapper;

    public FundingSourceService(FundingSourceRepository repo, UserRepository userRepository, FundingSourceMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public Iterable<ResponseFundingSourceDto> getSourcesByUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return repo.findByUserId(user.getId()).stream().map(mapper::toDto).toList();
    }

    public ResponseFundingSourceDto getFundingSourceById(long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(() -> new SourceNotFoundException(id)));
    }

    /**
     * Get a funding source by ID with user ownership verification.
     */
    public ResponseFundingSourceDto getFundingSourceByIdForUser(long id, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        FundingSource source = repo.findById(id)
            .orElseThrow(() -> new SourceNotFoundException(id));
        
        // Verify the source belongs to this user
        if (!source.getUser().getId().equals(user.getId())) {
            throw new SourceNotFoundException(id);
        }
        
        return mapper.toDto(source);
    }

    public ResponseFundingSourceDto createSourceForUser(FundingSourceDto sourceDto, String username) {
        // Find the user object in the DB
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        FundingSource source = mapper.toEntity(sourceDto);
        // Link the funding source to this specific user profile
        source.setUser(user);
        
        // Persist to database
        return mapper.toDto(repo.save(source));
    }


    public ResponseFundingSourceDto updateFundingSource(long id, FundingSourceDto dto) {
        var entity = repo.findById(id).orElseThrow(() -> new SourceNotFoundException(id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    /**
     * Update a funding source with user ownership verification.
     */
    public ResponseFundingSourceDto updateFundingSourceForUser(long id, FundingSourceDto dto, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        var entity = repo.findById(id).orElseThrow(() -> new SourceNotFoundException(id));
        
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new SourceNotFoundException(id);
        }
        
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    public void deleteFundingSource(long id) {
        repo.deleteById(id);
    }

    /**
     * Delete a funding source with user ownership verification.
     */
    public void deleteFundingSourceForUser(long id, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        FundingSource source = repo.findById(id)
            .orElseThrow(() -> new SourceNotFoundException(id));
        
        if (!source.getUser().getId().equals(user.getId())) {
            throw new SourceNotFoundException(id);
        }
        
        repo.deleteById(id);
    }

}
