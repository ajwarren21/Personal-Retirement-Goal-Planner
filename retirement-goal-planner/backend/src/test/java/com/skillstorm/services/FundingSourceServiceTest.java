package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.skillstorm.models.User;
import com.skillstorm.repositories.FundingSourceRepository;
import com.skillstorm.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class FundingSourceServiceTest {

    private static final String USERNAME = "testuser";

    @Mock
    private FundingSourceRepository repo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FundingSourceMapper mapper;

    @InjectMocks
    private FundingSourceService service;

    private User user;
    private FundingSource fundingSource;
    private FundingSourceDto fundingSourceDto;
    private ResponseFundingSourceDto responseFundingSourceDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);

        fundingSource = new FundingSource();
        fundingSource.setId(1L);
        fundingSource.setUser(user);

        fundingSourceDto = mock(FundingSourceDto.class);
        responseFundingSourceDto = mock(ResponseFundingSourceDto.class);
    }

    private FundingSource fundingSourceOwnedByOther() {
        User otherUser = new User();
        otherUser.setId(2L);

        FundingSource other = new FundingSource();
        other.setId(1L);
        other.setUser(otherUser);
        return other;
    }

    /**
     * Get sources by User valid
     */
    @Test
    void getSourcesByUserReturnsListOfDtos() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findByUserId(user.getId())).thenReturn(List.of(fundingSource));
        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        Iterable<ResponseFundingSourceDto> result = service.getSourcesByUser(USERNAME);

        assertEquals(List.of(responseFundingSourceDto), result);
        verify(repo, times(1)).findByUserId(user.getId());
    }

    /**
     * Get sources by user empyt
     */
    @Test
    void getSourcesByUserReturnsEmptyWhenNoneExist() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findByUserId(user.getId())).thenReturn(List.of());

        Iterable<ResponseFundingSourceDto> result = service.getSourcesByUser(USERNAME);

        assertFalse(result.iterator().hasNext());
    }

    /**
     * Get sources by user invalid
     */
    @Test
    void getSourcesByUserThrowsWhenUserNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getSourcesByUser(USERNAME));
    }

    
    /**
     * Get source by Id valid
     */
    @Test
    void getFundingSourceByIdForUserReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(fundingSource));
        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        ResponseFundingSourceDto result = service.getFundingSourceByIdForUser(1L, USERNAME);

        assertEquals(responseFundingSourceDto, result);
        verify(repo, times(1)).findById(1L);
    }

    /**
     * Get source by Id invalid
     */
    @Test
    void getFundingSourceByIdForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class, () -> service.getFundingSourceByIdForUser(200L, USERNAME));
    }

    /**
     * Get source by Id not owned by current user
     */
    @Test
    void getFundingSourceByIdForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(fundingSourceOwnedByOther()));

        assertThrows(SourceNotFoundException.class, () -> service.getFundingSourceByIdForUser(1L, USERNAME));
    }

    
    /**
     * Create new new source by user valid
     */
    @Test
    void createSourceForUserReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(mapper.toEntity(fundingSourceDto)).thenReturn(fundingSource);
        when(repo.save(fundingSource)).thenReturn(fundingSource);
        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        ResponseFundingSourceDto result = service.createSourceForUser(fundingSourceDto, USERNAME);

        assertEquals(responseFundingSourceDto, result);
        verify(repo, times(1)).save(fundingSource);
    }

    /**
     * Create new source by user invalid
     */
    @Test
    void createSourceForUserThrowsWhenUserNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.createSourceForUser(fundingSourceDto, USERNAME));
        verify(repo, never()).save(any());
    }

    
    /**
     * update source by user valid
     */
    @Test
    void updateFundingSourceForUserModifiesAndReturnsDto() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(fundingSource));
        doNothing().when(mapper).updateEntityFromDto(fundingSourceDto, fundingSource);
        when(repo.save(fundingSource)).thenReturn(fundingSource);
        when(mapper.toDto(fundingSource)).thenReturn(responseFundingSourceDto);

        ResponseFundingSourceDto result = service.updateFundingSourceForUser(1L, fundingSourceDto, USERNAME);

        assertEquals(responseFundingSourceDto, result);
        verify(mapper, times(1)).updateEntityFromDto(fundingSourceDto, fundingSource);
        verify(repo, times(1)).save(fundingSource);
    }

    /**
     * Update source by user invalid
     */
    @Test
    void updateFundingSourceForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class,
                () -> service.updateFundingSourceForUser(200L, fundingSourceDto, USERNAME));
        verify(repo, never()).save(any());
    }

    /**
     * update source by user not owned by current user
     */
    @Test
    void updateFundingSourceForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(fundingSourceOwnedByOther()));

        assertThrows(SourceNotFoundException.class,
                () -> service.updateFundingSourceForUser(1L, fundingSourceDto, USERNAME));
        verify(repo, never()).save(any());
    }

    
    /**
     * Delete source by user valid
     */
    @Test
    void deleteFundingSourceForUserDeletes() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(fundingSource));
        doNothing().when(repo).deleteById(1L);

        service.deleteFundingSourceForUser(1L, USERNAME);

        verify(repo, times(1)).deleteById(1L);
    }

    /**
     * delete source by user invalid
     */
    @Test
    void deleteFundingSourceForUserThrowsWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(200L)).thenReturn(Optional.empty());

        assertThrows(SourceNotFoundException.class, () -> service.deleteFundingSourceForUser(200L, USERNAME));
        verify(repo, never()).deleteById(anyLong());
    }

    /**
     * delete source by user not owned by current user
     */
    @Test
    void deleteFundingSourceForUserThrowsWhenNotOwnedByUser() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(repo.findById(1L)).thenReturn(Optional.of(fundingSourceOwnedByOther()));

        assertThrows(SourceNotFoundException.class, () -> service.deleteFundingSourceForUser(1L, USERNAME));
        verify(repo, never()).deleteById(anyLong());
    }
}