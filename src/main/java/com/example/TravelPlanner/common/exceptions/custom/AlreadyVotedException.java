package com.example.TravelPlanner.common.exceptions.custom;

public class AlreadyVotedException extends RuntimeException {
    public AlreadyVotedException(String user) {
        super(user + " already voted!");
    }
}
