package com.skillstorm.mappers;

import org.mapstruct.Mapper;
// import org.springframework.web.bind.annotation.Mapping;

// import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.models.RetirementGoal;
 
@Mapper(componentModel = "spring", uses = { com.skillstorm.mappers.ContributionMapper.class })
public interface RetirementGoalMapper {


    /**
     * Map from dto to entity
     * @param dto dto to map
     * @return RetirementGoal entity 
    */
    // @Mapping(target = "id", ignore = true)
    RetirementGoal toEntity(RetirementGoalDto dto);

    /**
     * Map from entity to Dto
     * @param entity entity to map
     * @return ResponseRetirementGoalDto
     */
    // @Mapping(target = "id", ignore = true)
    ResponseRetirementGoalDto toDto(RetirementGoal entity);

    /**
     * Updates the entity found in the database from the given dto
     * @param dto dto with fields to update to
     * @param entity entity to be updated
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "contributions", ignore = true)
    void updateEntityFromDto(RetirementGoalDto dto, @MappingTarget RetirementGoal entity);

}
