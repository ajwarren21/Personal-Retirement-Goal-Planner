package com.skillstorm.exceptions;

/**
 * Custom exception for not finding a Contribution in the database
 */
public class ContributionNotFoundException extends RuntimeException{

    public ContributionNotFoundException(Long id) {
        super("Contribution with id: " + id + " not found");
    }
}
