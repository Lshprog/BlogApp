package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;

import java.util.List;
import java.util.UUID;

public interface TravelPlanService {

    List<TravelPlan> listAllTravelPlansByUser(UUID userId);

    TravelPlan saveNewTravelPlan(TravelPlanDTO travelPlanDTO, User user);

}
