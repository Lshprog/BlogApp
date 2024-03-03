package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.common.exceptions.custom.UserNotPartOfTravelPlanException;
import com.example.TravelPlanner.travelplanning.dto.VoteDTO;
import com.example.TravelPlanner.travelplanning.dto.VotingDTO;

import java.util.UUID;

public interface VotingService {

    VotingDTO getVotingById(Long votingId);

    void deleteVoting(Long votingId);

    VotingDTO createNewVoting(VotingDTO votingDTO, UUID user_id);

    void makeVote(VoteDTO voteDTO, UUID user_id);

}
