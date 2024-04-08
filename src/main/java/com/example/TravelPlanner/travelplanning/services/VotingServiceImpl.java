package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.common.exceptions.custom.OverlappingEventsException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.common.utils.mappers.voting.VoteMapper;
import com.example.TravelPlanner.common.utils.mappers.voting.VotingMapper;
import com.example.TravelPlanner.travelplanning.common.CheckService;
import com.example.TravelPlanner.travelplanning.common.DynamicSchedulingService;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.dto.voting.*;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class VotingServiceImpl implements VotingService{

    private final CentralSupport centralSupport;
    private final VotingMapper votingMapper;
    private final VoteMapper voteMapper;
    private final CheckService checkService;
    private final DynamicSchedulingService dynamicSchedulingService;

    @Override
    public VotingDTO getVotingById(Long votingId, UUID userId) {
        return votingMapper.mapVotingToVotingDto(checkService.checkVotingExistence(votingId), userId);
    }

    @Override
    public void deleteVoting(Long votingId) {
        Voting voting = checkService.checkVotingExistence(votingId);
        voting.getEvent().setPlaceStatus(PlaceStatus.SUGGESTED);
        centralSupport.getEventRepository().save(voting.getEvent());
        centralSupport.getVotingRepository().deleteById(votingId);
    }

    @Override
    @Transactional
    public VotingDTO createNewVoting(VotingCreateDTO votingCreateDTO, UUID userId) {
        Event event = checkService.checkEventExistence(votingCreateDTO.getEventId());
        if (event.getPlaceStatus() != PlaceStatus.SUGGESTED) {
            return null;
        }
        List<Event> overlappingEvents = centralSupport.getEventRepository().findEventsByTravelPlanAndTimeAndPlaceStatus(
                event.getTravelPlan().getId(),
                PlaceStatus.VOTING,
                event.getStartTime(),
                event.getEndTime()
        );
        if (!overlappingEvents.isEmpty()){
            throw new OverlappingEventsException();
        }
        Voting voting = votingMapper.mapVotingCreateDTOtoVoting(votingCreateDTO,userId);
        try {
            voting = centralSupport.getVotingRepository().save(voting);
            event.setPlaceStatus(PlaceStatus.VOTING);
            centralSupport.getEventRepository().save(event);
            dynamicSchedulingService.scheduleVotingEventJob(voting.getId(), voting.getEndTime());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        return votingMapper.mapVotingToVotingDto(voting, userId);
    }

    @Override
    @Transactional
    @PreAuthorize("@permissionSecurityService.hasVotePermission(#votingId, #userId)")
    public void makeVote(VoteCreateDTO voteCreateDTO, Long votingId, UUID userId) {
        if (checkService.checkFinishedVoting(votingId)) centralSupport.getVotesRepository().save(voteMapper.mapVoteCreateDTOToVote(voteCreateDTO, votingId, userId));
    }

    public List getVotingsByTravelPlan(Long travelPlanId, UUID userId) {
        return centralSupport.getMapperUtil().mapListWithUser(centralSupport.getVotingRepository().findVotingsByTravelPlanId(travelPlanId), votingMapper::mapVotingToVotingDto, userId);
    }

    @Override
    public void deleteFinishedVotings(Long travelPlanId) {
        centralSupport.getVotingRepository().deleteFinishedVotings(travelPlanId);
    }
}
