package com.skillstorm.exceptions;

/**
 * Custom exception for not finding a Retirement Goal in the database
 */
public class GoalNotFoundException extends RuntimeException{

    public GoalNotFoundException(Long id) {
        super("Retirement Goal with id: " + id + " not found");
    }
}
