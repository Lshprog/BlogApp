package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.common.exceptions.custom.OverlappingEventsException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EventNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.VotingNotFoundException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.common.utils.mappers.voting.VoteMapper;
import com.example.TravelPlanner.common.utils.mappers.voting.VotingMapper;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.dto.voting.*;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class VotingServiceImpl implements VotingService{

    private final CentralSupport centralSupport;
    private final VotingMapper votingMapper;
    private final VoteMapper voteMapper;

    @Override
    public VotingDTO getVotingById(Long votingId) {
        Optional<Voting> optionalVoting = centralSupport.getVotingRepository().findById(votingId);
        if (optionalVoting.isEmpty()) {
            throw new VotingNotFoundException("No such voting");
        }
        return votingMapper.mapVotingToVotingDto(optionalVoting.get());
    }

    @Override
    public void deleteVoting(Long votingId) {
        Optional<Voting> optionalVoting = centralSupport.getVotingRepository().findById(votingId);
        if (optionalVoting.isEmpty()) {
            throw new VotingNotFoundException("No such voting");
        }
        Voting voting = optionalVoting.get();
        voting.getEvent().setPlaceStatus(PlaceStatus.SUGGESTED);
        centralSupport.getEventRepository().save(voting.getEvent());
        centralSupport.getVotingRepository().deleteById(votingId);

    }

    @Override
    @Transactional
    public VotingDTO createNewVoting(VotingCreateDTO votingCreateDTO, UUID userId) {
        Optional<Event> optionalEvent = centralSupport.getEventRepository().findById(votingCreateDTO.getEventId());
        if(optionalEvent.isEmpty()){
            throw new EventNotFoundException(votingCreateDTO.getEventId());
        }
        Event event = optionalEvent.get();
        List<Event> overlappingEvents = centralSupport.getEventRepository().findEventsByTravelPlanAndTimeAndPlaceStatus(
                event.getTravelPlan().getId(),
                PlaceStatus.VOTING,
                event.getStartTime(),
                event.getStartTime()
        );
        if (!overlappingEvents.isEmpty()){
            throw new OverlappingEventsException();
        }
        Voting voting = votingMapper.mapVotingCreateDTOtoVoting(votingCreateDTO,userId);
        voting = centralSupport.getVotingRepository().save(voting);
        event.setPlaceStatus(PlaceStatus.VOTING);
        centralSupport.getEventRepository().save(event);
        return votingMapper.mapVotingToVotingDto(voting);
    }

    // add annotation to check if voted
    @Override
    @Transactional
    @PreAuthorize("@permissionSecurityService.hasVotePermission(#votingId, #userId)")
    public void makeVote(VoteCreateDTO voteCreateDTO, Long votingId, UUID userId) {
        centralSupport.getVotesRepository().save(voteMapper.mapVoteCreateDTOToVote(voteCreateDTO, votingId, userId));
    }

    @Override
    public List<VotingPreviewDTO> getVotingsByTravelPlan(Long travelPlanId) {
        return centralSupport.getMapperUtil().mapList(centralSupport.getVotingRepository().findVotingsByTravelPlanId(travelPlanId), votingMapper::mapVotingToVotingPreviewDto);
    }
}
