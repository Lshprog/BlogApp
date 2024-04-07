package com.example.TravelPlanner.common.security;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.exceptions.custom.AlreadyVotedException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.VotesRepository;
import com.example.TravelPlanner.travelplanning.repositories.VotingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionSecurityService {

    private final CentralSupport centralSupport;

    public boolean hasVotePermission(Long votingId, UUID userId) {
        if (centralSupport.getVotesRepository().existsByCreatorIdAndVotingId(userId, votingId)) {
            throw new AlreadyVotedException();
        }
        return true;
    }

    public boolean hasEventChangePermission(Long eventId, UUID userId) {
        if (centralSupport.getEventRepository().getReferenceById(eventId).getCreator().getId() == userId) {
            throw new AlreadyVotedException();
        }
        return true;
    }

}
