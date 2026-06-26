package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.skillstorm.dtos.ResponseContributionDto;
// import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.dtos.ContributionDto;
// import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.exceptions.ContributionNotFoundException;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.exceptions.SourceNotFoundException;
import com.skillstorm.mappers.ContributionMapper;
import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.repositories.ContributionRepository;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.RetirementGoalRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ContributionServiceTest {
    @Mock
    private ContributionRepository repo;

    @Mock
    private RetirementGoalRepository goalRepo;

    @Mock
    private FundingSourceRepository fundingRepo;

    @Mock
    private ContributionMapper mapper;

    @InjectMocks
    private ContributionService service;

    @InjectMocks
    private FundingSourceService fundingService;

    @InjectMocks
    private RetirementGoalService goalService;

    private Contribution goal;
    private ContributionDto requestDto;
    private ResponseContributionDto responseDto;

    @BeforeEach
    void setup() {
        goal = new Contribution();
        goal.setId(1L);

        requestDto = mock(ContributionDto.class);
        responseDto = mock(ResponseContributionDto.class);
    }


    @Test
    void getAllValid() {
        when(repo.findAll()).thenReturn(List.of(goal));
        when(mapper.toDto(goal)).thenReturn(responseDto);

        Iterable<ResponseContributionDto> result = service.getAll();

        assertNotNull(result);

        verify(repo).findAll();
        verify(mapper).toDto(goal);
    }

    @Test 
    void getByIdValid() {
        when(repo.findById(1L)).thenReturn(Optional.of(goal));
        when(mapper.toDto(goal)).thenReturn(responseDto);

        ResponseContributionDto result = service.getById(1L);

        assertNotNull(result);
        assertEquals(responseDto, result);

        verify(repo).findById(1L);
        verify(mapper).toDto(goal);
    }

    @Test
    void getByIdInvalid() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        // when(mapper.toDto(goal)).thenReturn(responseDto);
        
        // ResponseContributionDto result = service.getById(1L);
        assertThrows(ContributionNotFoundException.class, () -> service.getById(999L));

        verify(repo).findById(999L);
    }

    @Test
    void createValid() {
        ContributionDto dto = mock(ContributionDto.class);
        // RetirementGoal goal = mock(RetirementGoal.class);
        // FundingSource funding = mock(FundingSource.class);

        RetirementGoal goal = new RetirementGoal();
        FundingSource funding = new FundingSource();
        goal.setId(1L);

        FundingSource source = new FundingSource();
        source.setId(2L);

        Contribution contribution = new Contribution();
        ResponseContributionDto responseDto = mock(ResponseContributionDto.class);

        when(dto.RetirementGoalId()).thenReturn(1L);
        when(dto.fundingSourceId()).thenReturn(2L);

        // when(dto.goal()).thenReturn(goal);
        // when(dto.fundingSource()).thenReturn(funding);

        // when(goal.getId()).thenReturn(1L);
        // when(funding.getId()).thenReturn(2L);

        when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));
        when(fundingRepo.findById(2L)).thenReturn(Optional.of(source));

        when(mapper.toEntity(dto)).thenReturn(contribution);
        when(repo.save(contribution)).thenReturn(contribution);
        when(mapper.toDto(contribution)).thenReturn(responseDto);

        System.out.println(funding.getId());

        ResponseContributionDto result = service.create(dto);

        assertNotNull(result);
        assertEquals(responseDto, result);
         
        verify(goalRepo).findById(1L);
        verify(fundingRepo).findById(2L);
        verify(mapper).toEntity(dto);
        verify(repo).save(contribution);
        verify(mapper).toDto(contribution);

        assertEquals(goal, contribution.getGoal());
        assertEquals(source, contribution.getFundingSource());
    }

    @Test
    void createInvalidGoal() {
        ContributionDto dto = mock(ContributionDto.class);
        RetirementGoal goal = new RetirementGoal();
        goal.setId(1L);

        when(dto.RetirementGoalId()).thenReturn(1L);

        // when(dto.RetirementGoalId()).thenReturn();
        // when(goal.getId()).thenReturn(1L);

        when(goalRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GoalNotFoundException.class, () -> service.create(dto));

        verify(goalRepo).findById(1L);
        // verifyNoInteractions(fundingRepo, repo, mapper);
    }

    @Test
    void createInvalidSource() {
        ContributionDto dto = mock(ContributionDto.class);
        // RetirementGoalDto goalDto = mock(RetirementGoalDto.class);
        // FundingSourceDto fundingDto = mock(FundingSourceDto.class);

        RetirementGoal goal = new RetirementGoal();
        FundingSource funding = new FundingSource();

        // RetirementGoal goal = new RetirementGoal();
        goal.setId(1L);
        funding.setId(2L);

        
        when(dto.RetirementGoalId()).thenReturn(1L);
        // when(dto.goal()).thenReturn(goal);
        when(dto.fundingSourceId()).thenReturn(2L);
        // when(dto.fundingSource()).thenReturn(funding);

        // when(goal.getId()).thenReturn(1L);
        // when(funding.getId()).thenReturn(2L);

        when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));
        when(fundingRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class, () -> service.create(dto));

        verify(goalRepo).findById(1L);
        verify(fundingRepo).findById(2L);
        // verifyNoInteractions(repo);
    }

    @Test
    void updateValid() {
        when(repo.findById(1L)).thenReturn(Optional.of(goal));
        when(mapper.toDto(goal)).thenReturn(responseDto);

        ResponseContributionDto result = service.update(1L, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);

        verify(repo).findById(1L);
        verify(mapper).updateEntityFromDto(requestDto, goal);
        verify(repo).save(goal);
        verify(mapper).toDto(goal);
    }

    @Test
    void updateInvalid() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ContributionNotFoundException.class, () -> service.update(999L, requestDto));

        verify(repo).findById(999L);
    }

    @Test
    void deleteValid() {
        when(repo.findById(1L)).thenReturn(Optional.of(goal));

        service.delete(1L);

        verify(repo).findById(1L);
        verify(repo).delete(goal);
    }

    @Test
    void deleteInvalid() {
        when(repo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ContributionNotFoundException.class, () -> service.delete(999L));
    }

}
