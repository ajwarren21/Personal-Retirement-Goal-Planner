package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.FundingSourceMapper;
import com.skillstorm.models.FundingSource;
import com.skillstorm.repositories.FundingSourceRepository;

@ExtendWith(MockitoExtension.class)
public class FundingSourceServiceTest {

    @Mock
    private FundingSourceRepository repo;

    @Mock
    private FundingSourceMapper mapper;

    @InjectMocks
    private FundingSourceService service;

    private FundingSource fundingSource;
    private FundingSourceDto fundingSourceDto;
    private ResponseFundingSourceDto responseFundingSourceDto;

    // Runs before each test - sets up the test data
    @BeforeEach
    void setUp() {
        fundingSource = new FundingSource();
        fundingSource.setId(1L);

        fundingSourceDto = mock(FundingSourceDto.class);
        responseFundingSourceDto = mock(ResponseFundingSourceDto.class);
    }


    @Test
    void getAllFundingSourcesReturnsListOfDtos() {

        when(repo.findAll()).thenReturn(List.of(fundingSource));

        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        Iterable<ResponseFundingSourceDto> result = service.getAllFundingSources();

        assertEquals(List.of(responseFundingSourceDto), result);

        verify(repo, times(1)).findAll();
    }

    @Test
    void getAllFundingSourcesReturnsEmptyWhenNoneExist() {

        when(repo.findAll()).thenReturn(List.of());

        Iterable<ResponseFundingSourceDto> result = service.getAllFundingSources();

        assertFalse(result.iterator().hasNext());
    }


    @Test
    void getFundingSourceByIdReturnsDto() {

        when(repo.findById(1L)).thenReturn(Optional.of(fundingSource));

        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        ResponseFundingSourceDto result = service.getFundingSourceById(1L);

        assertEquals(responseFundingSourceDto, result);

        verify(repo, times(1)).findById(1L);
    }

    @Test
    void getFundingSourceByIdThrowsExceptionWhenNotFound() {

        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class, () -> service.getFundingSourceById(200L));
    }


    @Test
    void createFundingSourceReturnsDto() {

        when(mapper.toEntity(fundingSourceDto)).thenReturn(fundingSource);

        when(repo.save(fundingSource)).thenReturn(fundingSource);

        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        ResponseFundingSourceDto result = service.createFundingSource(fundingSourceDto);

        assertEquals(responseFundingSourceDto, result);

        verify(repo, times(1)).save(fundingSource);
    }


    @Test
    void updateFundingSourceModifiesAndReturnsDto() {

        when(repo.findById(1L)).thenReturn(Optional.of(fundingSource));

        doNothing().when(mapper).updateEntityFromDto(fundingSourceDto, fundingSource);

        when(repo.save(fundingSource)).thenReturn(fundingSource);

        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        ResponseFundingSourceDto result = service.updateFundingSource(1L, fundingSourceDto);

        assertEquals(responseFundingSourceDto, result);

        verify(mapper, times(1)).updateEntityFromDto(fundingSourceDto, fundingSource);
        verify(repo, times(1)).save(fundingSource);
    }

    @Test
    void updateFundingSourceThrowsExceptionWhenNotFound() {

        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class, () -> service.updateFundingSource(200L, fundingSourceDto));

        verify(repo, never()).save(any());
    }


    @Test
    void deleteFundingSourceDeletes() {

        doNothing().when(repo).deleteById(1L);

        service.deleteFundingSource(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}