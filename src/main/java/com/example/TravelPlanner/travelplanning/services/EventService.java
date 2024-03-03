package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.travelplanning.dto.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;

import java.util.List;
import java.util.UUID;

public interface EventService {

    List<EventDTO> listAllEventsByTravelPlan(Long planId);
    EventDTO getEventById(Long eventId);
    EventDTO saveNewEvent(EventCreateDTO eventCreateDTO, UUID userId);
    EventDTO updateEvent(EventDTO eventDTO);
    void deleteEvent(Long eventId);
}
