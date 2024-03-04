package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanShowDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanUpdateDTO;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;

import java.util.List;
import java.util.UUID;

public interface TravelPlanService {

    List<TravelPlanShowDTO> listAllTravelPlansByUser(UUID userId);
    TravelPlanDTO getTravelPlanById(Long planId);
    TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID user);
    TravelPlanDTO updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, UUID userId);
    void deleteTravelPlan(Long travelPlanId, UUID userId);
    String generateNewInviteLink(Long travelPlanId);
    void leaveTravelPlan(TravelPlanDTO travelPlanDTO, UUID userId);
    List<UserPlanRoles> findPlanUsers(TravelPlanDTO travelPlanDTO);
    TravelPlanDTO joinTravelPlan(String joinCode, UUID userId);

}
