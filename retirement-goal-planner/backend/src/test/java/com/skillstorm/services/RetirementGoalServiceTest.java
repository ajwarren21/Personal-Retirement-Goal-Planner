// package com.skillstorm.services;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// import java.util.List;
// import java.util.Optional;

// import com.skillstorm.dtos.ResponseRetirementGoalDto;
// import com.skillstorm.dtos.RetirementGoalDto;
// import com.skillstorm.exceptions.GoalNotFoundException;
// import com.skillstorm.mappers.RetirementGoalMapper;
// import com.skillstorm.models.RetirementGoal;
// import com.skillstorm.repositories.RetirementGoalRepository;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;


// @ExtendWith(MockitoExtension.class)
// public class RetirementGoalServiceTest {
//     @Mock
//     private RetirementGoalRepository repo;

//     @Mock
//     private RetirementGoalMapper mapper;

//     @InjectMocks
//     private RetirementGoalService service;

//     private RetirementGoal goal;
//     private RetirementGoalDto requestDto;
//     private ResponseRetirementGoalDto responseDto;

//     @BeforeEach
//     void setup() {
//         goal = new RetirementGoal();
//         goal.setId(1L);

//         requestDto = mock(RetirementGoalDto.class);
//         responseDto = mock(ResponseRetirementGoalDto.class);
//     }


//     @Test
//     void getAllValid() {
//         when(repo.findAll()).thenReturn(List.of(goal));
//         when(mapper.toDto(goal)).thenReturn(responseDto);

//         Iterable<ResponseRetirementGoalDto> result = service.getAll();

//         assertNotNull(result);

//         // Check that the mapper and repo were called correctly
//         verify(repo).findAll();
//         verify(mapper).toDto(goal);
//     }

//     @Test 
//     void getByIdValid() {
//         when(repo.findById(1L)).thenReturn(Optional.of(goal));
//         when(mapper.toDto(goal)).thenReturn(responseDto);

//         ResponseRetirementGoalDto result = service.getById(1L);

//         assertNotNull(result);
//         assertEquals(responseDto, result);

//         verify(repo).findById(1L);
//         verify(mapper).toDto(goal);
//     }

//     @Test
//     void getByIdInvalid() {
//         when(repo.findById(999L)).thenReturn(Optional.empty());
//         // when(mapper.toDto(goal)).thenReturn(responseDto);
        
//         // ResponseRetirementGoalDto result = service.getById(1L);
//         assertThrows(GoalNotFoundException.class, () -> service.getById(999L));

//         verify(repo).findById(999L);
//     }

//     @Test
//     void createValid() {
//         when(mapper.toEntity(requestDto)).thenReturn(goal);
//         when(repo.save(goal)).thenReturn(goal);
//         when(mapper.toDto(goal)).thenReturn(responseDto);

//         ResponseRetirementGoalDto result = service.create(requestDto);

//         assertNotNull(result);
//         assertEquals(responseDto, result);

//         verify(mapper).toEntity(requestDto);
//         verify(repo).save(goal);
//         verify(mapper).toDto(goal);
//     }

//     @Test
//     void updateValid() {
//         when(repo.findById(1L)).thenReturn(Optional.of(goal));
//         when(mapper.toDto(goal)).thenReturn(responseDto);

//         ResponseRetirementGoalDto result = service.update(1L, requestDto);

//         assertNotNull(result);
//         assertEquals(responseDto, result);

//         verify(repo).findById(1L);
//         verify(mapper).updateEntityFromDto(requestDto, goal);
//         verify(repo).save(goal);
//         verify(mapper).toDto(goal);
//     }

//     @Test
//     void updateInvalid() {
//         when(repo.findById(999L)).thenReturn(Optional.empty());

//         assertThrows(GoalNotFoundException.class, () -> service.update(999L, requestDto));

//         verify(repo).findById(999L);
//     }

//     @Test
//     void deleteValid() {
//         when(repo.findById(1L)).thenReturn(Optional.of(goal));

//         service.delete(1L);

//         verify(repo).findById(1L);
//         verify(repo).delete(goal);
//     }

//     @Test
//     void deleteInvalid() {
//         when(repo.findById(999L)).thenReturn(Optional.empty());

//         assertThrows(GoalNotFoundException.class, () -> service.delete(999L));
//     }

// }
