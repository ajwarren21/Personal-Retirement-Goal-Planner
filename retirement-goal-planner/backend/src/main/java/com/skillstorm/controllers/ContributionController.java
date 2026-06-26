package com.skillstorm.controllers;


import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.dtos.ResponseContributionDto;
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
@RequestMapping("/goals")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"})
public class ContributionController {

    // private final ResponseContributionDto responseContributionDto;
    private final ContributionService service;

    public ContributionController(ContributionService service) {
        this.service = service;
        // this.responseContributionDto = responseContributionDto;
    }

    @GetMapping
    public ResponseEntity<Iterable<ResponseContributionDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseContributionDto> getById(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseContributionDto> create(@Valid @RequestBody ContributionDto dto) {
        ResponseContributionDto created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseContributionDto> update(@PathVariable long id, @Valid @RequestBody ContributionDto dto) {
        ResponseContributionDto updated = service.update(id, dto);
        // Could maybe return this another way, test first
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted dividend payment");
    }
    
    
}
