package com.notzed.rahmaApp.event_service.exception;

public class UnAuthorisedException extends RuntimeException {
    public UnAuthorisedException(String message) {
        super(message);
    }
}
