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
        
        // finding the user out of our database
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        /**
         * Need to convert roles into authorities
         *      Roles in Database: USER, ADMIN, MANAGER, etc.
         *      Spring Authorities: ROLE_USER, ROLE_ADMIN, ROLE_MANAGER, etc
         */
        Set<GrantedAuthority> authorities = user.getRoles().stream()
            .map(Role::getName)
            .map(name -> "ROLE_" + name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());

        /**
         * Spring Security has its won "User" object. You could name your user model
         * something like "AppUser" to avoid confusion with this Spring Security class
         */
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true,                   // accountNonExpired
            true,                   // credentialsNonExpired
            true,                   // accountNonLocked
            authorities
        );
        
    }

    @Transactional
    public User register(UserDto registeringUser) {

        // check if the username already exists. returns null if it does
        if(userRepository.existsByUsername(registeringUser.username())) {
            return null;
        }

        // All new users will default to only having the USER role
        Role userRole = roleRepository.findByName("USER")
            .orElseThrow(() -> new IllegalStateException("User role is missing from database."));

        User user = new User();
        user.setUsername(registeringUser.username());

        // making sure to hash password with bcrypt before putting in db
        user.setPassword(passwordEncoder.encode(registeringUser.password()));   
        user.setEnabled(true);
        user.setRoles(Set.of(userRole));

        return userRepository.save(user);
        
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
