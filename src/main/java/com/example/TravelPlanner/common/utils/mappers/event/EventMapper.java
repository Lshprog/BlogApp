package com.example.TravelPlanner.common.utils.mappers.event;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventMapper{

    private final MappingSupport mappingSupport;

    public EventDTO mapEventToEventDTO(Event event) {
        EventDTO eventDTO = mappingSupport.getMapperUtil().map(event, EventDTO.class);
        eventDTO.setCreator(event.getCreator().getUsername());
        eventDTO.setLoc(this.getEventLocation(event));
        return eventDTO;
    }

    public Event mapEventDTOToEvent(EventDTO eventDTO, Long travelPlanId) {
        Event event = mappingSupport.getMapperUtil().map(eventDTO, Event.class);
        event.setCreator(mappingSupport.getUserRepository().findByUsername(eventDTO.getCreator())
                .orElseThrow(() -> new UserNotFoundException(eventDTO.getCreator())));
        event.setTravelPlan(mappingSupport.getTravelPlanRepository().getReferenceById(travelPlanId));
        this.setEventLocation(event, eventDTO.getLoc());
        return event;
    }

    public Event mapEventCreateDTOtoEvent(EventCreateDTO eventDTO, Long travelPlanId, UUID userId) {
        Event event = mappingSupport.getMapperUtil().map(eventDTO, Event.class);
        event.setCreator(mappingSupport.getUserRepository().getReferenceById(userId));
        event.setTravelPlan(mappingSupport.getTravelPlanRepository().getReferenceById(travelPlanId));
        this.setEventLocation(event, eventDTO.getLoc());
        return event;
    }


    public void setEventLocation(Event curEvent, Location newLocation) {
        curEvent.setLoc(newLocation);
        curEvent.setLocation(mappingSupport.getMapperUtil().convertPojoToJson(newLocation));
    }

    public Location getEventLocation(Event curEvent) {
        if (curEvent.getLoc() == null && curEvent.getLocation() != null) {
            curEvent.setLoc(mappingSupport.getMapperUtil().convertJsonToPojo(curEvent.getLocation(), Location.class));
        }
        return curEvent.getLoc();
    }

}
