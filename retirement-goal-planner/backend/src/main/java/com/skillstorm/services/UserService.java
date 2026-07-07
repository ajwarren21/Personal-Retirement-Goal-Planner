package com.skillstorm.services;

import org.springframework.stereotype.Service;

import com.skillstorm.dtos.ResponseUserDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.exceptions.UserNotFoundException;
import com.skillstorm.mappers.UserMapper;
import com.skillstorm.models.User;
import com.skillstorm.repositories.UserRepository;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    
    private final UserRepository repo;
    private final UserMapper mapper;
    // private final PasswordEncoder pe;

    public UserService(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
        // this.pe = pe;
    }

    public Iterable<ResponseUserDto> getAll() {
        return repo.findAll().stream().map(mapper::toResponseDto).toList();
    }

    public ResponseUserDto getById(long id) {
        return mapper.toResponseDto(repo.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public ResponseUserDto create(UserDto dto) {
        return mapper.toResponseDto(repo.save(mapper.toEntity(dto)));
    }

    public ResponseUserDto update(long id, UserDto dto) {
        User entity = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        mapper.updateEntityFromDto(dto, entity);
        return mapper.toResponseDto(repo.save(entity));
    }

    public void delete(long id) {
        User entity = repo.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repo.delete(entity);
    }

    // public ResponseUserDto getUserByUsername(String email) {
    //     User user = repo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(null));

    //     return mapper.toResponseDto(user);
    // }


}
