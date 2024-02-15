package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.repositories.PlaceRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TravellingService implements TravelPlanService{

    private final PlaceRepository placeRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;


    @Override
    public List<TravelPlan> listAllTravelPlansByUser(UUID userId) {
        return travelPlanRepository.findTravelPlansByUserId(userId);
    }
}
