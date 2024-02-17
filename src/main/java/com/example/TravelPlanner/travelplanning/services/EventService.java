package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.dto.EventDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;

import java.util.List;
import java.util.UUID;

public interface EventService {

    List<EventDTO> listAllEventsByTravelPlan(Long planId);
    TravelPlan saveNewEvent(TravelPlanCreateDTO travelPlanCreateDTO, User user);
    void updateEvent(TravelPlanDTO travelPlanDTO, User user);
    void delete(TravelPlan travelPlanDTO, User user);
    String generateNewInviteLink(TravelPlanDTO travelPlanDTO, User user);
}
