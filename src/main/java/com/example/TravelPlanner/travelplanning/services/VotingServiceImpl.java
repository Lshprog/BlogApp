package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.AlreadyVotedException;
import com.example.TravelPlanner.common.exceptions.custom.OverlappingEventsException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EventNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.VotingNotFoundException;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.voting.VoteMapper;
import com.example.TravelPlanner.common.utils.mappers.voting.VotingMapper;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.dto.voting.*;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class VotingServiceImpl implements VotingService{

    private final MappingSupport mappingSupport;
    private final VotingMapper votingMapper;
    private final VoteMapper voteMapper;

    @Override
    public VotingDTO getVotingById(Long votingId) {
        Optional<Voting> optionalVoting = mappingSupport.getVotingRepository().findById(votingId);
        if (optionalVoting.isEmpty()) {
            throw new VotingNotFoundException("No such voting");
        }
        return votingMapper.mapVotingToVotingDto(optionalVoting.get());
    }

    @Override
    public void deleteVoting(Long votingId) {
        Optional<Voting> optionalVoting = mappingSupport.getVotingRepository().findById(votingId);
        if (optionalVoting.isEmpty()) {
            throw new VotingNotFoundException("No such voting");
        }
        Voting voting = optionalVoting.get();
        voting.getEvent().setPlaceStatus(PlaceStatus.SUGGESTED);
        mappingSupport.getEventRepository().save(voting.getEvent());
        mappingSupport.getVotingRepository().deleteById(votingId);

    }

    @Override
    @Transactional
    public VotingDTO createNewVoting(VotingCreateDTO votingCreateDTO, UUID userId) {
        Optional<Event> optionalEvent = mappingSupport.getEventRepository().findById(votingCreateDTO.getEventId());
        if(optionalEvent.isEmpty()){
            throw new EventNotFoundException(votingCreateDTO.getEventId());
        }
        Event event = optionalEvent.get();
        List<Event> overlappingEvents = mappingSupport.getEventRepository().findEventsByTravelPlanAndTimeAndPlaceStatus(
                event.getTravelPlan().getId(),
                PlaceStatus.VOTING,
                event.getStartTime(),
                event.getStartTime()
        );
        if (!overlappingEvents.isEmpty()){
            throw new OverlappingEventsException();
        }
        Voting voting = votingMapper.mapVotingCreateDTOtoVoting(votingCreateDTO,userId);
        voting = mappingSupport.getVotingRepository().save(voting);
        event.setPlaceStatus(PlaceStatus.VOTING);
        mappingSupport.getEventRepository().save(event);
        return votingMapper.mapVotingToVotingDto(voting);
    }

    // add annotation to check if voted
    @Override
    @Transactional
    @PreAuthorize("@permissionSecurityService.hasVotePermission(#votingId, #userId)")
    public void makeVote(VoteCreateDTO voteCreateDTO, Long votingId, UUID userId) {
        mappingSupport.getVotesRepository().save(voteMapper.mapVoteCreateDTOToVote(voteCreateDTO, votingId, userId));
    }

    @Override
    public List<VotingPreviewDTO> getVotingsByTravelPlan(Long travelPlanId) {
        return mappingSupport.getMapperUtil().mapList(mappingSupport.getVotingRepository().findVotingsByTravelPlanId(travelPlanId), votingMapper::mapVotingToVotingPreviewDto);
    }
}
