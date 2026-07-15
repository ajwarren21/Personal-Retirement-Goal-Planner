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

    Contribution toEntity(ContributionDto dto);

    @Mapping(target = "retirementGoalId", source = "goal.id")
    @Mapping(target = "retirementGoalName", source = "goal.name")
    @Mapping(target = "fundingSourceId", source = "fundingSource.id")
    @Mapping(target = "fundingSourceName", source = "fundingSource.name")
    ResponseContributionDto toDto(Contribution entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ContributionDto dto, @MappingTarget Contribution entity);

}
