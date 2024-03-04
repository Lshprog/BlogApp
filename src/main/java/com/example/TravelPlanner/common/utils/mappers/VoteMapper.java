package com.example.TravelPlanner.common.utils.mappers;

import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteMapper implements CustomMapper<Vote, VoteDTO> {

    private final MapperUtil mapperUtil;

    @Override
    public VoteDTO mapFw(Vote vote) {
        return new VoteDTO(vote.getCreator().getUsername(), vote.getVoting().getId(), vote.getDescription());
    }

    @Override
    public Vote mapBk(VoteDTO voteDTO) {
        return null;
    }
}
