package com.example.TravelPlanner.common.exceptions.custom;

import java.util.UUID;

public class AlreadyVotedException extends RuntimeException {
    public AlreadyVotedException() {
        super("Already voted!");
    }
}
