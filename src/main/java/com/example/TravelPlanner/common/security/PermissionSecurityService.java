package com.example.TravelPlanner.common.security;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.exceptions.custom.AlreadyVotedException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EventNotFoundException;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRolesRepository;
import com.example.TravelPlanner.travelplanning.repositories.VotesRepository;
import com.example.TravelPlanner.travelplanning.repositories.VotingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionSecurityService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final VotingRepository votingRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;
    private final VotesRepository votesRepository;

    public boolean hasEditorPermission(Long travelPlanId, UUID userId) {
        return (userPlanRolesRepository.getUserPlanRoleByUserIdAndTravelPlanId(travelPlanId, userId).getRole() != PlanRole.VOTER);
    }

    public boolean hasOwnerPermission(Long travelPlanId, UUID userId) {
        return (userPlanRolesRepository.getUserPlanRoleByUserIdAndTravelPlanId(travelPlanId, userId).getRole() == PlanRole.OWNER);
    }

    public boolean hasEventCreatorPermission(Long eventId, UUID userId) {
        return eventRepository.isCreator(eventId, userId);
    }

    public boolean hasVotePermission(Long votingId, UUID userId) {
        if (votesRepository.existsByCreatorIdAAndVotingId(userId, votingId)) {
            throw new AlreadyVotedException();
        }
        return true;
    }

//    public boolean hasVotingCreatorPermission(Long votingId, UUID userId) {
//        return votingRepository
//    }
}
