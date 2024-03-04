package com.example.TravelPlanner.common.utils.mappers;

import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingDTO;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VotingMapper implements CustomMapper<Voting, VotingDTO>{

    private final MapperUtil mapperUtil;
    private final VoteMapper voteMapper;

    @Override
    public VotingDTO mapToDTO(Voting voting) {
        VotingDTO votingDTO = mapperUtil.map(voting, VotingDTO.class);
        votingDTO.setCreator(voting.getCreator().getUsername());
        votingDTO.setVotes(mapperUtil.mapList(voting.getVotes(), voteMapper::mapFw));
        return votingDTO;
    }

    @Override
    public Voting mapToEntity(VotingDTO votingDTO) {
        return null;
    }
}
