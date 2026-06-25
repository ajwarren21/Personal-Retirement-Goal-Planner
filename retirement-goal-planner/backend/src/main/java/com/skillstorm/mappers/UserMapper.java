package com.skillstorm.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.skillstorm.dtos.ResponseUserDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a UserDto to a User entity. The id, password, and fundingSources fields are ignored during the mapping process.
     * @param dto
     * @return User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fundingSources", ignore = true)
    User toEntity(UserDto dto);

    /**
     * Maps a User entity to a ResponseUserDto.
     * @param entity
     * @return ResponseUserDto
     */
    ResponseUserDto toResponseDto(User entity);

    /**
     * Updates an existing User entity with values from a UserDto. The id, password, and fundingSources fields are ignored during the mapping process.
     * @param dto
     * @param entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fundingSources", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);

}
