package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingDTO;

import java.util.UUID;

public interface VotingService {

    VotingDTO getVotingById(Long votingId);

    void deleteVoting(Long votingId);

    VotingDTO createNewVoting(VotingCreateDTO votingDTO, UUID user_id);

    void makeVote(VoteDTO voteDTO, UUID user_id);

}
