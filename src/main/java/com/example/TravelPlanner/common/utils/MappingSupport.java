package com.example.TravelPlanner.common.utils;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.VotesRepository;
import com.example.TravelPlanner.travelplanning.repositories.VotingRepository;
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
}

