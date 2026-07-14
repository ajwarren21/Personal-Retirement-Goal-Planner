package com.skillstorm.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.skillstorm.dtos.FundingSourceDto;
import com.skillstorm.dtos.ResponseFundingSourceDto;
import com.skillstorm.models.FundingSource;
import com.skillstorm.services.FundingSourceService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/funding-source")
@CrossOrigin(origins = {"https://d1dpzej41c5vns.cloudfront.net", "https://d3p6jm11yfu3uu.cloudfront.net", "http://localhost:4200", "http://localhost:5500", "http://127.0.0.1:5500"})
public class FundingSourceController {

    private final FundingSourceService service;

    public FundingSourceController(FundingSourceService service) {
        this.service = service;
    }

   @GetMapping
    public ResponseEntity<Iterable<ResponseFundingSourceDto>> getUserFundingSources(Principal principal) {
        Iterable<ResponseFundingSourceDto> sources = service.getSourcesByUser(principal.getName());
        return ResponseEntity.ok(sources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseFundingSourceDto> getById(@PathVariable long id, Principal principal) {
        return ResponseEntity.ok(service.getFundingSourceByIdForUser(id, principal.getName()));
    }


    @PostMapping
    public ResponseEntity<ResponseFundingSourceDto> createFundingSource(
            @RequestBody FundingSourceDto newSource, 
            Principal principal) {
        
        // 1. Extract the username/email of the logged-in user from the Principal
        String username = principal.getName();
        
        // 2. Pass both the object data and the user identifier to your service layer
        ResponseFundingSourceDto savedSource = service.createSourceForUser(newSource, username);
        
        return new ResponseEntity<>(savedSource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseFundingSourceDto> update(@PathVariable long id, @Valid @RequestBody FundingSourceDto dto, Principal principal) {
        ResponseFundingSourceDto updated = service.updateFundingSourceForUser(id, dto, principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, Principal principal) {
        service.deleteFundingSourceForUser(id, principal.getName());
        return ResponseEntity.noContent().build();

    }
    

}
