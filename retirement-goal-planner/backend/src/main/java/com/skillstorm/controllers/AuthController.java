package com.skillstorm.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.UserDto;
import com.skillstorm.dtos.UpdateUserDto;
import com.skillstorm.dtos.ResponseUserDto;
import com.skillstorm.models.User;
import com.skillstorm.services.UserService;

import jakarta.validation.Valid;

import java.security.Principal;
import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"https://d1dpzej41c5vns.cloudfront.net", "https://d3p6jm11yfu3uu.cloudfront.net", "http://localhost:4200", "http://localhost:5500", "http://127.0.0.1:5500"})
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

    private ResponseUserDto toResponseDto(User user) {
    return new ResponseUserDto(user.getId(), user.getUsername(), user.getEmail());
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseUserDto> registerNewUser(@Valid @RequestBody UserDto registeringUser) {   
    User createdUser = userService.register(registeringUser);
    if(createdUser == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    return new ResponseEntity<>(toResponseDto(createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(Principal principal, HttpServletRequest request) {
        // If the security filter already authenticated the request (e.g., via Basic), principal will be present
        if (principal != null) {
            User loggedInUser = userService.getUserByUsername(principal.getName());
            return ResponseEntity.ok(toResponseDto(loggedInUser));
        }

        // Otherwise attempt manual authentication using Authorization header (Basic)
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        try {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
            final String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values.length > 1 ? values[1] : "";

            var authRequest = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(username, password);
            var authResult = authenticationManager.authenticate(authRequest);

            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authResult);
            // Ensure session is created so subsequent requests with cookie are authenticated
            request.getSession(true);

            User loggedInUser = userService.getUserByUsername(username);
            return ResponseEntity.ok(toResponseDto(loggedInUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
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