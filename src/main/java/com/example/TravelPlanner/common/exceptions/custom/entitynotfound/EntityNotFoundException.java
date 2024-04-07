package com.example.TravelPlanner.common.exceptions.custom.entitynotfound;

import com.example.TravelPlanner.travelplanning.entities.TravelPlan;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

}
