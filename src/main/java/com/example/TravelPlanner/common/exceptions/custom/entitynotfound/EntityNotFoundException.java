package com.example.TravelPlanner.common.exceptions.custom.entitynotfound;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
