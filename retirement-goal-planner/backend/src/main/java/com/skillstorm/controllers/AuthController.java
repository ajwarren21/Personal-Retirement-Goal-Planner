package com.skillstorm.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.dtos.UpdateUserDto;
import com.skillstorm.dtos.ResponseUserDto;
import com.skillstorm.models.User;
import com.skillstorm.services.UserService;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/csrf")
    public ResponseEntity<Map<String, String>> getCsrfToken(CsrfToken csrf) {
        Map<String, String> body = new LinkedHashMap<>();

        body.put("headerName", csrf.getHeaderName());
        body.put("token", csrf.getToken());
        return ResponseEntity.ok(body);
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@Valid @RequestBody UserDto registeringUser) {
    
        User createdUser = userService.register(registeringUser);
        if(createdUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(Principal principal) {
    
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        User loggedInUser = userService.getUserByUsername(principal.getName());
        
        return ResponseEntity.ok(loggedInUser);
    }

    @PutMapping("/user")
    public ResponseEntity<ResponseUserDto> updateUser(Principal principal, @Valid @RequestBody UpdateUserDto updateRequest) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ResponseUserDto updatedUser = userService.updateUser(principal.getName(), updateRequest);
        if (updatedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(updatedUser);
    }
   

    

}