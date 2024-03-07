package com.example.TravelPlanner.common.exceptions.custom.entitynotfound;

public class VotingNotFoundException extends EntityNotFoundException{
    public VotingNotFoundException(String message) {
        super(message);
    }
    public VotingNotFoundException(Long id) { super("Voting not found with id: " + id); }
}
