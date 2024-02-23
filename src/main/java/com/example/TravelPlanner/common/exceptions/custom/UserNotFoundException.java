package com.example.TravelPlanner.common.exceptions.custom;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException{
    public UserNotFoundException(UUID id){
        super("User not found for ID: " + id);
    }
    public UserNotFoundException(String username){
        super("User not found with username: " + username);
    }
}