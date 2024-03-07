package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EventNotFoundException;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.event.EventMapper;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class EventServiceImpl implements EventService{

    private final MappingSupport mappingSupport;
    private final EventMapper eventMapper;

    @Override
    public List<EventDTO> listAllEventsByTravelPlan(Long planId) {
        return mappingSupport.getMapperUtil().mapList(mappingSupport.getEventRepository().findNotSomePlaceStatusEvents(planId, PlaceStatus.VOTING), eventMapper::mapEventToEventDTO);
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Optional<Event> optionalEvent = mappingSupport.getEventRepository().findById(eventId);
        if (optionalEvent.isEmpty()){
            throw new EventNotFoundException(eventId);
        }
        return eventMapper.mapEventToEventDTO(optionalEvent.get());
    }

    @Override
    @Transactional
    public EventDTO saveNewEvent(EventCreateDTO eventCreateDTO, UUID userId, Long travelPlanId) {
        Event newEvent = eventMapper.mapEventCreateDTOtoEvent(eventCreateDTO, travelPlanId, userId);
        newEvent = mappingSupport.getEventRepository().save(newEvent);
        return eventMapper.mapEventToEventDTO(newEvent);
    }

    @Transactional
    @Override
    @PreAuthorize("@permissionSecurityService.hasEventCreatorPermission(#eventDTO.id, #travelPlanId)")
    public void updateEvent(EventDTO eventDTO, Long travelPlanId) {
//        Optional<Event> eventOptional = mappingSupport.getEventRepository().findById(eventDTO.getId());
//        if(eventOptional.isEmpty()){
//            throw new EventNotFoundException(eventDTO.getId());
//        }
//        Event event = eventOptional.get();
        Event event = mappingSupport.getEventRepository().findById(eventDTO.getId()).get();
        if(eventDTO.getPlaceStatus() == PlaceStatus.CONCRETE &&
                (event.getPlaceStatus() == PlaceStatus.SUGGESTED || event.getPlaceStatus() == PlaceStatus.VOTING)) {
            mappingSupport.getEventRepository().updateEventStatusByTravelPlanAndTime(
                    travelPlanId,
                    event.getPlaceStatus(),
                    event.getPlaceStatus(),
                    event.getStartTime(),
                    event.getEndTime()
            );
            event.setPlaceStatus(PlaceStatus.CONCRETE);

        } else {
            event.setPlaceStatus(PlaceStatus.SUGGESTED);
        }

        eventMapper.setEventLocation(event, eventDTO.getLoc());
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartTime(eventDTO.getStartTime());
        event.setEndTime(eventDTO.getEndTime());
        mappingSupport.getEventRepository().save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        mappingSupport.getEventRepository().deleteById(eventId);
    }

}
