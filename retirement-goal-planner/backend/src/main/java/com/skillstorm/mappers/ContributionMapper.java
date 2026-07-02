package com.skillstorm.mappers;

import org.mapstruct.Mapper;
// import org.springframework.web.bind.annotation.Mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.dtos.ContributionDto;
import com.skillstorm.models.Contribution;
 
@Mapper(componentModel = "spring")
public interface ContributionMapper {


    /**
     * Map from dto to entity
     * @param dto dto to map
     * @return Contribution entity 
    */
    // @Mapping(target = "id", ignore = true)
    Contribution toEntity(ContributionDto dto);

    /**
     * Map from entity to Dto
     * @param entity entity to map
     * @return ResponseContributionDto
     */
    @Mapping(target = "id", ignore = true)
    ResponseContributionDto toDto(Contribution entity);

    /**
     * Updates the entity found in the database from the given dto
     * @param dto dto with fields to update to
     * @param entity entity to be updated
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ContributionDto dto, @MappingTarget Contribution entity);

}
