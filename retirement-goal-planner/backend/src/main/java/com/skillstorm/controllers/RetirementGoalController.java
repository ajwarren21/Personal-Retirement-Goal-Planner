package com.skillstorm.controllers;


import java.security.Principal;

import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.dtos.ResponseRetirementGoalDto;
import com.skillstorm.dtos.RetirementGoalDto;
import com.skillstorm.services.RetirementGoalService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/goals")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:5500", "http://127.0.0.1:5500"})
public class RetirementGoalController {

    // private final ResponseRetirementGoalDto responseRetirementGoalDto;
    private final RetirementGoalService service;

    public RetirementGoalController(RetirementGoalService service) {
        this.service = service;
        // this.responseRetirem``entGoalDto = responseRetirementGoalDto;
    }

    @GetMapping
    public ResponseEntity<Iterable<ResponseRetirementGoalDto>> getUserRetirementGoals(Principal principal) {
        Iterable<ResponseRetirementGoalDto> goals = service.getGoalsByUser(principal.getName());
        // goals.forEach(goal -> System.out.println("Retirement Goal ID: " + goal.id()));
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRetirementGoalDto> getById(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(service.getByIdForUser(id, principal.getName()));
    }

    
    @PostMapping
    public ResponseEntity<ResponseRetirementGoalDto> createRetirementGoal(
            @RequestBody RetirementGoalDto newGoal, 
            Principal principal) {
        
        // 1. Extract the username/email of the logged-in user from the Principal
        String username = principal.getName();
        
        // 2. Pass both the object data and the user identifier to your service layer
        ResponseRetirementGoalDto savedGoal = service.createGoalForUser(newGoal, username);
        
        return new ResponseEntity<>(savedGoal, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRetirementGoalDto> update(@PathVariable long id, @Valid @RequestBody RetirementGoalDto dto, Principal principal) {
        ResponseRetirementGoalDto updated = service.updateForUser(id, dto, principal.getName());
        // Could maybe return this another way, test first
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id, Principal principal) {
        service.deleteForUser(id, principal.getName());
        return ResponseEntity.ok("Deleted dividend payment");
    }
    
    
}
