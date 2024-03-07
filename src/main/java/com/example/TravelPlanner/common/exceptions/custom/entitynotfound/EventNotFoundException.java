package com.example.TravelPlanner.common.exceptions.custom.entitynotfound;

public class EventNotFoundException extends EntityNotFoundException {
    public EventNotFoundException(Long id) {
        super("Event not found for ID: " + id);
    }
}
