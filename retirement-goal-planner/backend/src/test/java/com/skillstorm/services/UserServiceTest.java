package com.skillstorm.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.skillstorm.dtos.ResponseUserDto;
import com.skillstorm.dtos.UpdateUserDto;
import com.skillstorm.dtos.UserDto;
import com.skillstorm.models.Role;
import com.skillstorm.models.User;
import com.skillstorm.repositories.RoleRepository;
import com.skillstorm.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {

        role = new Role();
        role.setId(1);
        // role.setId(0);
        role.setName("USER");

        user = new User();
        user.setId(1L);
        user.setUsername("gagetest");
        user.setPassword("encodedPassword");
        user.setEmail("gage@test.com");
        user.setEnabled(true);
        user.setRoles(Set.of(role));
    }

    /**
     * loadUserByUsername
     */

    @Test
    void loadUserByUsernameReturnsUserDetails() {

        when(userRepository.findByUsername("gagetest"))
                .thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("gagetest");

        assertEquals("gagetest", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertTrue(result.isEnabled());
        assertTrue(
                result.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByUsername("gagetest");
    }

    @Test
    void loadUserByUsernameThrowsWhenUserNotFound() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing"));

        verify(userRepository).findByUsername("missing");
    }

    /**
     * register
     */

    @Test
    void registerCreatesUser() {

        UserDto dto = new UserDto(
            "new@test.com",
            "password",
            "newuser");

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        User result = service.register(dto);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("new@test.com", result.getEmail());
        assertTrue(result.isEnabled());
        assertEquals(Set.of(role), result.getRoles());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerReturnsNullWhenUsernameExists() {

        UserDto dto = new UserDto(
            "test@test.com",
            "password",
                "gagetest");

        when(userRepository.existsByUsername("gagetest"))
                .thenReturn(true);

        User result = service.register(dto);

        assertNull(result);

        verify(userRepository, never()).save(any());
        verify(roleRepository, never()).findByName(anyString());
    }

    @Test
    void registerCreatesRoleIfMissing() {

        UserDto dto = new UserDto(
            "new@test.com",
            "password",
            "newuser");

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(roleRepository.findByName("USER"))
                .thenReturn(Optional.empty());

        when(roleRepository.save(any(Role.class)))
                .thenReturn(role);

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        User result = service.register(dto);

        assertNotNull(result);

        verify(roleRepository).save(any(Role.class));
        verify(userRepository).save(any(User.class));
    }

    /**
     * getAllUsers
     */

    @Test
    void getAllUsersReturnsUsers() {

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<User> result = service.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user, result.get(0));

        verify(userRepository).findAll();
    }

    @Test
    void getAllUsersReturnsEmptyList() {

        when(userRepository.findAll())
                .thenReturn(List.of());

        List<User> result = service.getAllUsers();

        assertTrue(result.isEmpty());
    }

    /**
     * getUserByUsername
     */

    @Test
    void getUserByUsernameReturnsUser() {

        when(userRepository.findByUsername("gagetest"))
                .thenReturn(Optional.of(user));

        User result = service.getUserByUsername("gagetest");

        assertEquals(user, result);

        verify(userRepository).findByUsername("gagetest");
    }

    @Test
    void getUserByUsernameThrowsWhenMissing() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.getUserByUsername("missing"));
    }

    /**
     * updateUser
     */

    @Test
    void updateUserUpdatesUsernameAndEmail() {

        UpdateUserDto dto =
                new UpdateUserDto("newuser", "new@test.com");

        when(userRepository.findByUsername("gagetest"))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(userRepository.save(user))
                .thenReturn(user);

        ResponseUserDto result =
                service.updateUser("gagetest", dto);

        assertEquals("newuser", result.username());
        assertEquals("new@test.com", result.email());

        verify(userRepository).save(user);
    }

    @Test
    void updateUserReturnsNullWhenUsernameAlreadyExists() {

        UpdateUserDto dto =
                new UpdateUserDto("taken", "new@test.com");

        when(userRepository.findByUsername("gagetest"))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByUsername("taken"))
                .thenReturn(true);

        ResponseUserDto result =
                service.updateUser("gagetest", dto);

        assertNull(result);

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserAllowsKeepingSameUsername() {

        UpdateUserDto dto =
                new UpdateUserDto("gagetest", "updated@test.com");

        when(userRepository.findByUsername("gagetest"))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        ResponseUserDto result =
                service.updateUser("gagetest", dto);

        assertEquals("gagetest", result.username());
        assertEquals("updated@test.com", result.email());

        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserThrowsWhenCurrentUserMissing() {

        UpdateUserDto dto =
                new UpdateUserDto("newuser", "new@test.com");

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.updateUser("missing", dto));

        verify(userRepository, never()).save(any());
    }
}