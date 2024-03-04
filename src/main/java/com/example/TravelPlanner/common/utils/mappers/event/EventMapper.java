package com.example.TravelPlanner.common.utils.mappers.event;

import com.example.TravelPlanner.common.exceptions.custom.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.CustomMapper;
import com.example.TravelPlanner.common.utils.mappers.VotingMapper;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMapper extends AbstractEventMapper implements CustomMapper<Event, EventDTO> {

    @Autowired
    public EventMapper(MappingSupport mappingSupport, VotingMapper votingMapper) {
        super(mappingSupport, votingMapper);
    }

    @Override
    public EventDTO mapToDTO(Event event) {
        EventDTO eventDTO = mappingSupport.getMapperUtil().map(event, EventDTO.class);
        eventDTO.setCreator(event.getCreator().getUsername());
        eventDTO.setTravelPlanId(event.getTravelPlan().getId());
        eventDTO.setVoting(votingMapper.mapFw(event.getVoting()));
        eventDTO.setLoc(this.getEventLocation(event));
        return eventDTO;
    }

    @Override
    public Event mapToEntity(EventDTO eventDTO) {
        Event event = mappingSupport.getMapperUtil().map(eventDTO, Event.class);
        event.setCreator(userRepository.findByUsername(eventDTO.getCreator())
                .orElseThrow(() -> new UserNotFoundException(eventDTO.getCreator())));
        event.setTravelPlan(travelPlanRepository.findById(eventDTO.getTravelPlanId())
                .orElseThrow(() -> new TravelPlanNotFoundException(eventDTO.getId())));
        event.setVoting(votingMapper.mapBk(eventDTO.getVoting()));
        this.setEventLocation(event, eventDTO.getLoc());
        return event;
    }

}
