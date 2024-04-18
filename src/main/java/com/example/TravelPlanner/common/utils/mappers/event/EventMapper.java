package com.example.TravelPlanner.common.utils.mappers.event;

import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.UserNotFoundException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventMapper{

    private final CentralSupport centralSupport;

    public EventDTO mapEventToEventDTO(Event event) {
        EventDTO eventDTO = centralSupport.getMapperUtil().map(event, EventDTO.class);
        eventDTO.setCreator(event.getCreator().getUsername());
        return eventDTO;
    }

    public Event mapEventDTOToEvent(EventDTO eventDTO, Long travelPlanId) {
        Event event = centralSupport.getMapperUtil().map(eventDTO, Event.class);
        event.setTravelPlan(centralSupport.getTravelPlanRepository().getReferenceById(travelPlanId));
        return event;
    }

    public Event mapEventCreateDTOtoEvent(EventCreateDTO eventDTO, Long travelPlanId, UUID userId) {
        Event event = centralSupport.getMapperUtil().map(eventDTO, Event.class);
        event.setCreator(centralSupport.getUserRepository().getReferenceById(userId));
        event.setTravelPlan(centralSupport.getTravelPlanRepository().getReferenceById(travelPlanId));
        return event;
    }

}
