package com.skillstorm.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.models.User;
import com.skillstorm.services.UserService;

import jakarta.validation.Valid;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
            // return 409 - CONFLICT if the username already exists
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    

}