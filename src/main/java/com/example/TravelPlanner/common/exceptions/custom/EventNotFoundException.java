package com.example.TravelPlanner.common.exceptions.custom;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(Long id) {
        super("Event not found for ID: " + id);
    }
}
