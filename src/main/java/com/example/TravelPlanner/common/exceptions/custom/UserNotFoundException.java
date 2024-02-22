package com.example.TravelPlanner.common.exceptions.custom;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException{
    public UserNotFoundException(UUID id){
        super("User not found for ID: " + id);
    }
}