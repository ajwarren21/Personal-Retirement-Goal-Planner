package com.skillstorm.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.models.Role;
import com.skillstorm.models.User;
import com.skillstorm.repositories.RoleRepository;
import com.skillstorm.repositories.UserRepository;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

   
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        
         
        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(Role::getName)
            .map(name -> "ROLE_" + name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true,
            true,
            true,
            authorities
        );
        
    }

    @Transactional
    public User register(UserDto registeringUser) {

        if(userRepository.existsByUsername(registeringUser.username())) {
            return null;
        }

        Role userRole = roleRepository.findByName("USER")
            .orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName("USER");
                return roleRepository.save(newRole);
            });

       
        User user = new User();
        user.setUsername(registeringUser.username());

        user.setPassword(passwordEncoder.encode(registeringUser.password()));   
        user.setEnabled(true);
        user.setRoles(Set.of(userRole));
        user.setEmail(registeringUser.email());

        return userRepository.save(user);
        
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


}