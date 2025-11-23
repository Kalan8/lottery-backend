package com.example.hibernatedemo.exception;

public class NoPlayersAvailableException extends RuntimeException {

    public NoPlayersAvailableException(String message) {
        super(message);
    }
}
