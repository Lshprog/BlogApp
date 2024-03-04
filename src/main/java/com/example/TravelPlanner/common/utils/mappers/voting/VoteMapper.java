package com.example.TravelPlanner.common.utils.mappers.voting;

import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMapper {
    public VoteDTO mapVoteToVoteDto(Vote vote) {
        return new VoteDTO(vote.getCreator().getUsername(), vote.getVoting().getId(), vote.getDescription());
    }

}
