package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.VotingDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.Voting;

public interface VotingService {

    VotingDTO createNewVoting(VotingDTO votingDTO);

    void makeVote(Long votingId);

}
