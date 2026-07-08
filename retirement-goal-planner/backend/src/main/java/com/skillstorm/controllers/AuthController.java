package com.skillstorm.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.models.User;
import com.skillstorm.services.UserService;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
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
   

    

}