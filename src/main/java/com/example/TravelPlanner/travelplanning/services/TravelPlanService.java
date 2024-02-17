package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;

import java.util.List;
import java.util.UUID;

public interface TravelPlanService {

    List<TravelPlanDTO> listAllTravelPlansByUser(UUID userId);
    TravelPlan saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, User user);
    void updateTravelPlan(TravelPlanDTO travelPlanDTO, User user);
    void delete(TravelPlanDTO travelPlanDTO, User user);
    String generateNewInviteLink(TravelPlanDTO travelPlanDTO, User user);

}
