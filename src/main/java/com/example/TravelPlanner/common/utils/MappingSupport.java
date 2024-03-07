package com.example.TravelPlanner.common.utils;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.utils.mappers.MapperUtil;
import com.example.TravelPlanner.travelplanning.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MappingSupport {

    private final MapperUtil mapperUtil;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final VotingRepository votingRepository;
    private final VotesRepository votesRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;

    // Getter methods for each dependency
    public MapperUtil getMapperUtil() {
        return mapperUtil;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public EventRepository getEventRepository() {
        return eventRepository;
    }

    public TravelPlanRepository getTravelPlanRepository() {
        return travelPlanRepository;
    }

    public VotingRepository getVotingRepository() {
        return votingRepository;
    }

    public VotesRepository getVotesRepository() {
        return votesRepository;
    }

    public UserPlanRolesRepository getUserPlanRolesRepository() {
        return userPlanRolesRepository;
    }
}

