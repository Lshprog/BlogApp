package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;

import java.util.List;
import java.util.UUID;

public interface TravelPlanService {

    List<TravelPlanDTO> listAllTravelPlansByUser(UUID userId);
    TravelPlan getTravelPlanById(Long planId);
    TravelPlan saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID user);
    void updateTravelPlan(TravelPlanDTO travelPlanDTO);
    void delete(TravelPlanDTO travelPlanDTO);
    String generateNewInviteLink(TravelPlanDTO travelPlanDTO);

}
