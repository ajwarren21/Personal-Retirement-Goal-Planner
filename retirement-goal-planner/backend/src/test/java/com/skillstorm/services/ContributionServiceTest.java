package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.skillstorm.dtos.ResponseContributionDto;
// import com.skillstorm.dtos.ResponseFundingSourceDto;
// import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.dtos.ContributionDto;
// import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.exceptions.ContributionNotFoundException;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.exceptions.SourceNotFoundException;
// import com.skillstorm.exceptions.ContributionNotFoundException;
import com.skillstorm.mappers.ContributionMapper;
import com.skillstorm.models.Contribution;
import com.skillstorm.models.FundingSource;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.User;
import com.skillstorm.repositories.ContributionRepository;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.RetirementGoalRepository;
import com.skillstorm.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ContributionServiceTest {
    
    private static final String USERNAME = "testuser";

    @Mock
    private UserRepository userRepository;

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

    private User user;
    private Contribution cont;
    private ContributionDto requestDto;
    private ResponseContributionDto responseDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);

        cont = new Contribution();
        cont.setId(1L);
        cont.setUser(user);

        requestDto = mock(ContributionDto.class);
        responseDto = mock(ResponseContributionDto.class);
    }

    private Contribution contributionOwnedByOther() {
        User otherUser = new User();
        otherUser.setId(2L);

        Contribution other = new Contribution();
        other.setId(1L);
        other.setUser(otherUser);
        return other;
    }


    @Test
    void getContributionsByUserReturnsListOfDtos() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findByUserId(user.getId())).thenReturn(List.of(cont));
        when(mapper.toDto(cont)).thenReturn(responseDto);


        Iterable<ResponseContributionDto> result = service.getContributionsByUser(USERNAME);

        assertNotNull(result);

        verify(repo).findByUserId(user.getId());
        verify(mapper).toDto(cont);
    }

    /**
     * Get contributions by user empty
     */
    @Test
    void getContributionsByUserReturnsEmptyWhenNoneExist() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findByUserId(user.getId())).thenReturn(List.of());

        Iterable<ResponseContributionDto> result = service.getContributionsByUser(USERNAME);

        assertFalse(result.iterator().hasNext());
    }

    /**
     * Get contributions by user invalid
     */
    @Test
    void getContributionsByUserThrowsWhenUserNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getContributionsByUser(USERNAME));
    }

 /**
     * Get contributions by Id valid
     */
    @Test
    void getContributionByIdForUserReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(cont));
        when(mapper.toDto(cont)).thenReturn(responseDto);

        ResponseContributionDto result = service.getByIdForUser(1L, USERNAME);

        assertEquals(responseDto, result);
        verify(repo, times(1)).findById(1L);
    }

    /**
     * Get contribution by Id invalid
     */
    @Test
    void getContributionByIdForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(ContributionNotFoundException.class, () -> service.getByIdForUser(200L, USERNAME));
    }

    /**
     * Get contribution by Id not owned by current user
     */
    @Test
    void getContributionByIdForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(contributionOwnedByOther()));

        assertThrows(ContributionNotFoundException.class, () -> service.getByIdForUser(1L, USERNAME));
    }

    @Test
    void createValid() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        ContributionDto dto = mock(ContributionDto.class);
        // RetirementGoal cont = mock(RetirementGoal.class);
        // FundingSource funding = mock(FundingSource.class);

        RetirementGoal goal = new RetirementGoal();
        FundingSource funding = new FundingSource();
        goal.setId(1L);
        goal.setUser(user);

        FundingSource source = new FundingSource();
        source.setId(2L);
        source.setUser(user);

        Contribution contribution = new Contribution();
        ResponseContributionDto responseDto = mock(ResponseContributionDto.class);

        when(dto.retirementGoalId()).thenReturn(1L);
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

        ResponseContributionDto result = service.createContributionForUser(dto, USERNAME);

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
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        ContributionDto dto = mock(ContributionDto.class);
        RetirementGoal goal = new RetirementGoal();
        goal.setId(1L);
        goal.setUser(user);

        when(dto.retirementGoalId()).thenReturn(1L);

        // when(dto.RetirementGoalId()).thenReturn();
        // when(goal.getId()).thenReturn(1L);

        when(goalRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(GoalNotFoundException.class, () -> service.createContributionForUser(dto, USERNAME));

        verify(goalRepo).findById(1L);
        // verifyNoInteractions(fundingRepo, repo, mapper);
    }

    @Test
    void createInvalidSource() {
        ContributionDto dto = mock(ContributionDto.class);
        // RetirementGoalDto goalDto = mock(RetirementGoalDto.class);
        // FundingSourceDto fundingDto = mock(FundingSourceDto.class);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        RetirementGoal goal = new RetirementGoal();
        FundingSource funding = new FundingSource();

        // RetirementGoal goal = new RetirementGoal();
        goal.setId(1L);
        funding.setId(2L);
        goal.setUser(user);
        funding.setUser(user);

        
        when(dto.retirementGoalId()).thenReturn(1L);
        // when(dto.goal()).thenReturn(goal);
        when(dto.fundingSourceId()).thenReturn(2L);
        // when(dto.fundingSource()).thenReturn(funding);

        // when(goal.getId()).thenReturn(1L);
        // when(funding.getId()).thenReturn(2L);

        when(goalRepo.findById(1L)).thenReturn(Optional.of(goal));
        when(fundingRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class, () -> service.createContributionForUser(dto, USERNAME));

        verify(goalRepo).findById(1L);
        verify(fundingRepo).findById(2L);
        // verifyNoInteractions(repo);
    }

    @Test
    void updateValid() {
        when(repo.findById(1L)).thenReturn(Optional.of(cont));
        when(mapper.toDto(cont)).thenReturn(responseDto);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        ResponseContributionDto result = service.updateForUser(1L, requestDto, USERNAME);

        assertNotNull(result);
        assertEquals(responseDto, result);

        verify(repo).findById(1L);
        verify(mapper).updateEntityFromDto(requestDto, cont);
        verify(repo).save(cont);
        verify(mapper).toDto(cont);
    }

    @Test
    void updateInvalid() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        assertThrows(ContributionNotFoundException.class, () -> service.updateForUser(999L, requestDto, USERNAME));

        verify(repo).findById(999L);
    }

    @Test
    void deleteValid() {
        when(repo.findById(1L)).thenReturn(Optional.of(cont));
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        

        service.deleteForUser(1L, USERNAME);

        verify(repo).findById(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void deleteInvalid() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        // when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(ContributionNotFoundException.class, () -> service.deleteForUser(999L, USERNAME));
        verify(repo, never()).deleteById(anyLong());

        // assertThrows(ContributionNotFoundException.class, () -> service.delete(999L));
    }

}
