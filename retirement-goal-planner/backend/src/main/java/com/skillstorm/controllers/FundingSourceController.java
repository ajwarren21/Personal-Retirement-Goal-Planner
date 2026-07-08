package com.skillstorm.controllers;

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
import com.skillstorm.services.FundingSourceService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/funding-source")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:5500", "http://127.0.0.1:5500"})
public class FundingSourceController {

    private final FundingSourceService service;

    public FundingSourceController(FundingSourceService service) {
        this.service = service;
    }

    // @GetMapping
    // public ResponseEntity<Iterable<ResponseFundingSourceDto>> getAll(@AuthenticationPrincipal String email) {
        
    //     return ResponseEntity.ok(service.getAllFundingSources(email));
    // }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseFundingSourceDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getFundingSourceById(id));
    }


    @PostMapping
    public ResponseEntity<ResponseFundingSourceDto> create(@Valid @RequestBody FundingSourceDto dto) {
        ResponseFundingSourceDto created = service.createFundingSource(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseFundingSourceDto> update(@PathVariable long id, @Valid @RequestBody FundingSourceDto dto) {
        ResponseFundingSourceDto updated = service.updateFundingSource(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.deleteFundingSource(id);
        return ResponseEntity.ok("Deleted Funding Source Successfully");
    }
    

}
