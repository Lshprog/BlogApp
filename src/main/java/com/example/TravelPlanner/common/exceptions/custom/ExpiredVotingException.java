package com.example.TravelPlanner.common.exceptions.custom;

public class ExpiredVotingException extends RuntimeException{

    public ExpiredVotingException(Long votingid) {
        super("Expired voting with id : " + votingid);
    }

}
