// package com.skillstorm.services;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doNothing;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.skillstorm.dtos.ResponseUserDto;
// import com.skillstorm.dtos.UserDto;
// import com.skillstorm.exceptions.UserNotFoundException;
// import com.skillstorm.mappers.UserMapper;
// import com.skillstorm.models.User;
// import com.skillstorm.repositories.UserRepository;

// @ExtendWith(MockitoExtension.class)
// public class UserServiceTest {

//     @Mock
//     private UserRepository repo;

//     @Mock
//     private UserMapper mapper;

//     @InjectMocks
//     private UserService service;

//     private User user;
//     private UserDto userDto;
//     private ResponseUserDto responseUserDto;

//     // Runs before each test - sets up the test data
//     @BeforeEach
//     void setUp() {
//         user = new User();
//         user.setId(1L);
//         user.setUsername("gagetest");
//         user.setPassword("password");
//         user.setEmail("gage@test.com");

//         userDto = mock(UserDto.class);
//         responseUserDto = mock(ResponseUserDto.class);

//     }


//     @Test
//     void getAllReturnsListOfDtos() {
//         when(repo.findAll()).thenReturn(List.of(user));

//         when(mapper.toResponseDto(user)).thenReturn(responseUserDto);

//         Iterable<ResponseUserDto> result = service.getAll();


//         assertEquals(List.of(responseUserDto), result);

//         verify(repo, times(1)).findAll();
//     }

//     @Test
//     void getAllReturnsEmptyWhenNoUsersFound() {
//         when(repo.findAll()).thenReturn(List.of());

//         Iterable<ResponseUserDto> result = service.getAll();

//         assertFalse(result.iterator().hasNext());
//     }

//     @Test
//     void getByIdReturnsResponseDto() {
//         when(repo.findById(1L)).thenReturn(Optional.of(user));
//         when(mapper.toResponseDto(user)).thenReturn(responseUserDto);

//         ResponseUserDto result = service.getById(1L);

//         assertEquals(responseUserDto, result);

//         verify(repo, times(1)).findById(1l);
//     }

//     @Test
//     void getByIdThrowsExceptionWhenNotFound() {
//         when(repo.findById(200L)).thenReturn(Optional.empty());

//         assertThrows(UserNotFoundException.class, () -> service.getById(200L));
//     }

//     @Test
//     void createAndReturnsResponseDto() {
//         when(mapper.toEntity(userDto)).thenReturn(user);
//         when(repo.save(user)).thenReturn(user);
//         when(mapper.toResponseDto(user)).thenReturn(responseUserDto);

//         ResponseUserDto result = service.create(userDto);

//         assertEquals(responseUserDto, result);

//         verify(repo, times(1)).save(user);

//     }

//     @Test
//     void updateAndReturnsDto() {
//         when(repo.findById(1L)).thenReturn(Optional.of(user));

//         doNothing().when(mapper).updateEntityFromDto(userDto, user);

//         when(repo.save(user)).thenReturn(user);

//         when(mapper.toResponseDto(user)).thenReturn(responseUserDto);

//         ResponseUserDto result = service.update(1L, userDto);

//         assertEquals(responseUserDto, result);

//         verify(mapper, times(1)).updateEntityFromDto(userDto, user);

//         verify(repo, times(1)).save(user);
//     }

//     @Test
//     void updateThrowsExceptionWhenNotFound() {
//         when(repo.findById(200l)).thenReturn(Optional.empty());

//         assertThrows(UserNotFoundException.class, () -> service.update(200L, userDto));

//         verify(repo, never()).save(any());
//     }

//     @Test
//     void deleteDeletesUser() {

//         when(repo.findById(1L)).thenReturn(Optional.of(user));

//         service.delete(1L);

//         verify(repo, times(1)).delete(user);

//     }
    
//     @Test
//     void deleteThrowsExceptionWhenNotFound() {

//         when(repo.findById(200L)).thenReturn(Optional.empty());

//         assertThrows(UserNotFoundException.class, () -> service.delete(200L));

//         verify(repo, never()).delete(any());
//     }
    


// }
