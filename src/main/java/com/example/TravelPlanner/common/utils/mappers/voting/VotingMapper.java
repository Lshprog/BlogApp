package com.example.TravelPlanner.common.utils.mappers.voting;

import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.event.EventMapper;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingPreviewDTO;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VotingMapper {
    private final MappingSupport mappingSupport;
    private final EventMapper eventMapper;
    private final VoteMapper voteMapper;

    public VotingDTO mapVotingToVotingDto(Voting voting) {
        VotingDTO votingDTO = mappingSupport.getMapperUtil().map(voting, VotingDTO.class);
        votingDTO.setCreator(voting.getCreator().getUsername());
        votingDTO.setVotes(mappingSupport.getMapperUtil().mapList(voting.getVotes(), voteMapper::mapVoteToVoteDto));
        votingDTO.setEvent(eventMapper.mapEventToEventDTO(voting.getEvent()));
        return votingDTO;
    }

    public VotingPreviewDTO mapVotingToVotingPreviewDto(Voting voting) {
        VotingPreviewDTO votingDTO = mappingSupport.getMapperUtil().map(voting, VotingPreviewDTO.class);
        votingDTO.setCreator(voting.getCreator().getUsername());
        votingDTO.setEventTitle(voting.getEvent().getTitle());
        return votingDTO;
    }

    public Voting mapVotingCreateDTOtoVoting(VotingCreateDTO votingCreateDTO, UUID userId) {
        Voting voting = mappingSupport.getMapperUtil().map(votingCreateDTO, Voting.class);
        voting.setCreator(mappingSupport.getUserRepository().getReferenceById(userId));
        voting.setEvent(mappingSupport.getEventRepository().getReferenceById(votingCreateDTO.getEventId()));
        return voting;
    }

}
