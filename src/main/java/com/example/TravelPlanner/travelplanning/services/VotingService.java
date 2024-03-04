package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingPreviewDTO;

import java.util.List;
import java.util.UUID;

public interface VotingService {

    VotingDTO getVotingById(Long votingId);

    void deleteVoting(Long votingId);

    VotingDTO createNewVoting(VotingCreateDTO votingDTO, UUID user_id);

    void makeVote(VoteDTO voteDTO, UUID user_id);

    List<VotingPreviewDTO> getVotingsByTravelPlan(Long travelPlanId);

}
