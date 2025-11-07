package com.example.hibernatedemo.exception;

/**
 * Exception thrown when a User entity is not found in the database.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
