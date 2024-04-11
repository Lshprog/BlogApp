package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.voting.*;

import java.util.List;
import java.util.UUID;

public interface VotingService {

    VotingDTO getVotingById(Long votingId, UUID userId);

    void deleteVoting(Long votingId);

    VotingDTO createNewVoting(Long votingId, UUID userId);

    void makeVote(VoteCreateDTO voteCreateDTO, Long votingId, UUID userId);

    List<VotingDTO> getVotingsByTravelPlan(Long travelPlanId, UUID userId);

    void deleteFinishedVotings(Long travelPlanId);

}
