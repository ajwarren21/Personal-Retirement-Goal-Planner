package com.skillstorm.controllers;


import java.security.Principal;

import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.ResponseContributionDto;
import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.dtos.ContributionDto;
import com.skillstorm.services.ContributionService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// import com.skillstorm.dto.ResponseContributionDto;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/contributions")
@CrossOrigin(origins = {"https://d1dpzej41c5vns.cloudfront.net", "https://d3p6jm11yfu3uu.cloudfront.net", "http://localhost:4200", "http://localhost:5500", "http://127.0.0.1:5500"})
public class ContributionController {

    // private final ResponseContributionDto responseContributionDto;
    private final ContributionService service;

    public ContributionController(ContributionService service) {
        this.service = service;
        // this.responseContributionDto = responseContributionDto;
    }

    @GetMapping
    public ResponseEntity<Iterable<ResponseContributionDto>> getUserContributions(Principal principal) {
        Iterable<ResponseContributionDto> conts = service.getContributionsByUser(principal.getName());
        return ResponseEntity.ok(conts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseContributionDto> getById(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(service.getByIdForUser(id, principal.getName()));
    }

    @PostMapping
    public ResponseEntity<ResponseContributionDto> createContribution(
            @RequestBody ContributionDto newCont, 
            Principal principal) {
        
        // 1. Extract the username/email of the logged-in user from the Principal
        String username = principal.getName();
        
        // 2. Pass both the object data and the user identifier to your service layer
        ResponseContributionDto savedCont = service.createContributionForUser(newCont, username);
        
        return new ResponseEntity<>(savedCont, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseContributionDto> update(@PathVariable long id, @Valid @RequestBody ContributionDto dto, Principal principal) {
        ResponseContributionDto updated = service.updateForUser(id, dto, principal.getName());
        // Could maybe return this another way, test first
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id, Principal principal) {
        service.deleteForUser(id, principal.getName());
        return ResponseEntity.ok("Deleted dividend payment");
    }
    
    
}
