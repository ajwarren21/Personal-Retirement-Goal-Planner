package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ResponseUserDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.mappers.UserMapper;
import com.skillstorm.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository repo;
    private final UserMapper mapper;

    public UserService(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Iterable<ResponseUserDto> getAll() {
        return repo.findAll().stream().map(mapper::toResponseDto).toList();
    }

    public ResponseUserDto getById(long id) {
        return mapper.toResponseDto(repo.findById(id).orElseThrow());
    }

    public ResponseUserDto create(UserDto dto) {
        return mapper.toResponseDto(repo.save(mapper.toEntity(dto)));
    }

    public ResponseUserDto update(long id, UserDto dto) {
        var entity = repo.findById(id).orElseThrow();
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponseDto(repo.save(entity));
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

}
