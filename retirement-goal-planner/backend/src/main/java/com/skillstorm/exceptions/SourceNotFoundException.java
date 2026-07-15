package com.skillstorm.exceptions;

/**
 * Custom exception for not finding a Funding Source in the database
 */
public class SourceNotFoundException extends RuntimeException{

    public SourceNotFoundException(Long id) {
        super("Funding Source with id: " + id + " not found");
    }
}
