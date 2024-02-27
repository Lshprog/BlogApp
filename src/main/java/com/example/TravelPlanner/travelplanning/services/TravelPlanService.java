package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;

import java.util.List;
import java.util.UUID;

public interface TravelPlanService {

    List<TravelPlanDTO> listAllTravelPlansByUser(UUID userId);
    TravelPlanDTO getTravelPlanById(Long planId);
    TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID user);
    void updateTravelPlan(TravelPlanDTO travelPlanDTO);
    void delete(TravelPlanDTO travelPlanDTO);
    String generateNewInviteLink(TravelPlanDTO travelPlanDTO);
    void leaveTravelPlan(TravelPlanDTO travelPlanDTO, UUID userId);
    List<UserPlanRoles> findPlanUsers(TravelPlanDTO travelPlanDTO);

}
