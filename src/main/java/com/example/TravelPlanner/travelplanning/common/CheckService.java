package com.example.TravelPlanner.travelplanning.common;

import com.example.TravelPlanner.common.exceptions.custom.ExpiredVotingException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EventNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.VotingNotFoundException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckService {

    private final CentralSupport centralSupport;

    public TravelPlan checkTravelPlanExistence(Long planId) {
        Optional<TravelPlan> optionalTravelPlan = centralSupport.getTravelPlanRepository().findById(planId);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(planId);
        }
        return optionalTravelPlan.get();
    }

    public TravelPlan checkTravelPlanExistence(String joinCode) {
        Optional<TravelPlan> optionalTravelPlan = centralSupport.getTravelPlanRepository().getTravelPlanByJoinCode(joinCode);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(joinCode);
        }
        return optionalTravelPlan.get();
    }

    public Event checkEventExistence(Long eventId) {
        Optional<Event> optionalEvent = centralSupport.getEventRepository().findById(eventId);
        if (optionalEvent.isEmpty()){
            throw new EventNotFoundException(eventId);
        }
        return optionalEvent.get();
    }

    public Voting checkVotingExistence(Long votingId) {
        Optional<Voting> optionalVoting = centralSupport.getVotingRepository().findById(votingId);
        if (optionalVoting.isEmpty()){
            throw new VotingNotFoundException(votingId);
        }
        return optionalVoting.get();
    }

    public boolean checkFinishedVoting(Long votingId) {
        Optional<Voting> optionalVoting = centralSupport.getVotingRepository().findById(votingId);
        if (optionalVoting.isEmpty()){
            throw new VotingNotFoundException(votingId);
        }
        Voting voting =  optionalVoting.get();
        if (voting.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ExpiredVotingException(votingId);
        }
        return true;
    }

    public void checkVotingResults(Long votingId) {
        Voting voting = centralSupport.getVotingRepository().findById(votingId).get();
        List<Vote> votes = voting.getVotes();
        Integer likes = 0;
        Integer dislikes = 0;
        for(Vote vote : votes){
            if(vote.getIsLiked()) likes++;
            else dislikes++;
        }
        Event event = voting.getEvent();
        if((double) likes / (likes + dislikes) < 0.8) {
            event.setPlaceStatus(PlaceStatus.SUGGESTED);
        } else {
            centralSupport.getEventRepository().updateEventStatusByTravelPlanAndTime(
                    event.getTravelPlan().getId(),
                    PlaceStatus.CONCRETE,
                    PlaceStatus.SUGGESTED,
                    event.getStartTime(),
                    event.getEndTime()
            );
            event.setPlaceStatus(PlaceStatus.CONCRETE);
        }
        centralSupport.getEventRepository().save(event);

    }
}
