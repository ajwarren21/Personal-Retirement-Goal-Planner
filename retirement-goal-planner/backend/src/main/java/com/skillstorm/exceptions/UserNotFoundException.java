package com.skillstorm.exceptions;


/**
 * Custom exception for not finding a User in the database
 */
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("User with id: " + id + " not found");
    }
}
