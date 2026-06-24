package com.skillstorm.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.models.FundingSource;

@Mapper(componentModel = "spring")
public interface FundingSourceMapper {

    /**
     * Maps a FundingSourceDto to a FundingSource entity. The id and user fields are ignored during the mapping process.
     * @param dto
     * @return FundingSource entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    FundingSource toEntity(FundingSourceDto dto);

    /**
     * Maps a FundingSource entity to a ResponseFundingSourceDto.
     * @param entity
     * @return ResponseFundingSourceDto
     */
    ResponseFundingSourceDto toDto(FundingSource entity);

    /**
     * Updates an existing FundingSource entity with values from a FundingSourceDto. The id and user fields are ignored during the mapping process.
     * @param dto
     * @param entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(FundingSourceDto dto, @MappingTarget FundingSource entity);
}
