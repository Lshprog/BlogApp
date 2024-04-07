package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanPreviewDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface TravelPlanService {

    List<TravelPlanPreviewDTO> listAllTravelPlansByUser(UUID userId);
    TravelPlanDTO getTravelPlanById(Long planId);
    TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID user);
    void updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, Long travelPLanId, UUID userId);
    void deleteTravelPlan(Long travelPlanId, UUID userId);
    String generateNewInviteLink(Long travelPlanId, UUID userId);
    void leaveTravelPlan(Long travelPlanId, UUID userId);
    List<String> findPlanUsers(Long travelPlanId);
    TravelPlanDTO joinTravelPlan(String joinCode, UUID userId);

}
