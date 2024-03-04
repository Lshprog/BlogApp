package com.example.TravelPlanner.common.utils.mappers.event;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.exceptions.custom.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.VotingMapper;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public abstract class AbstractEventMapper {
    protected final MappingSupport mappingSupport;
    protected final VotingMapper votingMapper;
    protected final EventRepository eventRepository = mappingSupport.getEventRepository();
    protected final UserRepository userRepository = mappingSupport.getUserRepository();
    protected final TravelPlanRepository travelPlanRepository = mappingSupport.getTravelPlanRepository();

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
