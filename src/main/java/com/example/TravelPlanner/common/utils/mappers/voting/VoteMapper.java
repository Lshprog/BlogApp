package com.example.TravelPlanner.common.utils.mappers.voting;

import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.travelplanning.dto.voting.VoteCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VoteMapper {

    private final MappingSupport mappingSupport;

    public VoteDTO mapVoteToVoteDto(Vote vote) {
        return VoteDTO.builder()
                .creator(vote.getCreator().getUsername())
                .description(vote.getDescription())
                .isLiked(vote.getIsLiked())
                .build();
    }

    public Vote mapVoteCreateDTOToVote(VoteCreateDTO voteCreateDTO, Long votingId, UUID userId) {
        Vote vote = mappingSupport.getMapperUtil().map(voteCreateDTO, Vote.class);
        vote.setCreator(mappingSupport.getUserRepository().getReferenceById(userId));
        vote.setVoting(mappingSupport.getVotingRepository().getReferenceById(votingId));
        return vote;
    }


}
