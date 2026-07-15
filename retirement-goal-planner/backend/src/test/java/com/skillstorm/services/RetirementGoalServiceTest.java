package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.exceptions.GoalNotFoundException;
import com.skillstorm.mappers.RetirementGoalMapper;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.models.RetirementGoal;
import com.skillstorm.repositories.RetirementGoalRepository;
import com.skillstorm.repositories.UserRepository;
import com.skillstorm.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class RetirementGoalServiceTest {

    private static final String USERNAME = "testuser";

    @Mock
    private RetirementGoalRepository repo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RetirementGoalMapper mapper;

    @InjectMocks
    private RetirementGoalService service;

    private User user;
    private RetirementGoal goal;
    private RetirementGoalDto requestDto;
    private ResponseRetirementGoalDto responseDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);

        goal = new RetirementGoal();
        goal.setId(1L);
        goal.setUser(user);

        requestDto = mock(RetirementGoalDto.class);
        responseDto = mock(ResponseRetirementGoalDto.class);
    }

    
    private RetirementGoal goalOwnedByOther() {
        User otherUser = new User();
        otherUser.setId(2L);

        RetirementGoal other = new RetirementGoal();
        other.setId(1L);
        other.setUser(otherUser);
        return other;
    }


    /**
     * Get goals by User valid
     */
    @Test
    void getGoalsByUserReturnsListOfDtos() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findByUserId(user.getId())).thenReturn(List.of(goal));
        when(mapper.toDto(goal)).thenReturn(responseDto);

        Iterable<ResponseRetirementGoalDto> result = service.getGoalsByUser(USERNAME);

        assertEquals(List.of(responseDto), result);
        verify(repo, times(1)).findByUserId(user.getId());
    }
    /**
     * Get goals by user empty
     */
    @Test
    void getGoalsByUserReturnsEmptyWhenNoneExist() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findByUserId(user.getId())).thenReturn(List.of());

        Iterable<ResponseRetirementGoalDto> result = service.getGoalsByUser(USERNAME);

        assertFalse(result.iterator().hasNext());
    }

    /**
     * Get goals by user invalid
     */
    @Test
    void getGoalsByUserThrowsWhenUserNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getGoalsByUser(USERNAME));
    }

    
    /**
     * Get goal by Id valid
     */
    @Test
    void getByIdForUserReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(goal));
        when(mapper.toDto(goal)).thenReturn(responseDto);

        ResponseRetirementGoalDto result = service.getByIdForUser(1L, USERNAME);

        assertEquals(responseDto, result);
        verify(repo, times(1)).findById(1L);
    }

    /**
     * Get goal by Id invalid
     */
    @Test
    void getByIdForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(GoalNotFoundException.class, () -> service.getByIdForUser(200L, USERNAME));
    }

    /**
     * Get goal by Id not owned by current user
     */
    @Test
    void getByIdForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(goalOwnedByOther()));

        assertThrows(GoalNotFoundException.class, () -> service.getByIdForUser(1L, USERNAME));
    }

    
    /**
     * Create new new goal by user valid
     */
    @Test
    void createGoalForUserReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(mapper.toEntity(requestDto)).thenReturn(goal);
        when(repo.save(goal)).thenReturn(goal);
        when(mapper.toDto(goal)).thenReturn(responseDto);

        ResponseRetirementGoalDto result = service.createGoalForUser(requestDto, USERNAME);

        assertEquals(responseDto, result);
        verify(repo, times(1)).save(goal);
    }

    /**
     * Create new goal by user invalid
     */
    @Test
    void createGoalForUserThrowsWhenUserNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.createGoalForUser(requestDto, USERNAME));
        verify(repo, never()).save(any());
    }

    
    /**
     * update goal by user valid
     */
    @Test
    void updateRetirementGoalForUserModifiesAndReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(goal));
        doNothing().when(mapper).updateEntityFromDto(requestDto, goal);
        when(repo.save(goal)).thenReturn(goal);
        when(mapper.toDto(goal)).thenReturn(responseDto);

        ResponseRetirementGoalDto result = service.updateForUser(1L, requestDto, USERNAME);

        assertEquals(responseDto, result);
        verify(mapper, times(1)).updateEntityFromDto(requestDto, goal);
        verify(repo, times(1)).save(goal);
    }

    /**
     * Update goal by user invalid
     */
    @Test
    void updateRetirementGoalForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(GoalNotFoundException.class,
                () -> service.updateForUser(200L, requestDto, USERNAME));
        verify(repo, never()).save(any());
    }

    /**
     * update goal by user not owned by current user
     */
    @Test
    void updateRetirementGoalForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(goalOwnedByOther()));

        assertThrows(GoalNotFoundException.class,
                () -> service.updateForUser(1L, requestDto, USERNAME));
        verify(repo, never()).save(any());
    }

    
    /**
     * Delete goal by user valid
     */
    @Test
    void deleteForUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(goal));
        doNothing().when(repo).deleteById(1L);

        service.deleteForUser(1L, USERNAME);

        verify(repo, times(1)).deleteById(1L);
    }

    /**
     * delete goal by user invalid
     */
    @Test
    void deleteForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(GoalNotFoundException.class, () -> service.deleteForUser(200L, USERNAME));
        verify(repo, never()).deleteById(anyLong());
    }

    /**
     * delete goal by user not owned by current user
     */
    @Test
    void deleteForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(goalOwnedByOther()));

        assertThrows(GoalNotFoundException.class, () -> service.deleteForUser(1L, USERNAME));
        verify(repo, never()).deleteById(anyLong());
    }
}
