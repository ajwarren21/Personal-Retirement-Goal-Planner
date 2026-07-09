package com.skillstorm.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.FundingSourceMapper;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.User;
import com.skillstorm.repositories.FundingSourceRepository;
// import com.skillstorm.repositories.UserRepository;
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

    public FundingSource createSourceForUser(FundingSource source, String username) {
        // Find the user object in the DB
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Link the funding source to this specific user profile
        source.setUser(user);
        
        // Persist to database
        return repo.save(source);
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
