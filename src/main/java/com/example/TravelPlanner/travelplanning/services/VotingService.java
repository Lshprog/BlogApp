package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.voting.*;

import java.util.List;
import java.util.UUID;

public interface VotingService {

    VotingDTO getVotingById(Long votingId);

    void deleteVoting(Long votingId);

    VotingDTO createNewVoting(VotingCreateDTO votingDTO, UUID userId);

    void makeVote(VoteCreateDTO voteCreateDTO, Long votingId, UUID userId);

    List<VotingPreviewDTO> getVotingsByTravelPlan(Long travelPlanId);

    void deleteFinishedVotings(Long travelPlanId);

}
