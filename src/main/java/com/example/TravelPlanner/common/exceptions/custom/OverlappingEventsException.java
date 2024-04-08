package com.example.TravelPlanner.common.exceptions.custom;

public class OverlappingEventsException extends RuntimeException {
    public OverlappingEventsException() { super("Overlapping events! "); }
}
