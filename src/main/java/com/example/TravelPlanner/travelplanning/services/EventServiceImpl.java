package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.common.exceptions.custom.BadRequest;
import com.example.TravelPlanner.common.exceptions.custom.NoPermissionException;
import com.example.TravelPlanner.common.exceptions.custom.OverlappingEventsException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EventNotFoundException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.common.utils.mappers.event.EventMapper;
import com.example.TravelPlanner.travelplanning.common.CheckService;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class EventServiceImpl implements EventService{

    private final CentralSupport centralSupport;
    private final EventMapper eventMapper;
    private final CheckService checkService;

    @Override
    public List<EventDTO> listAllEventsByTravelPlan(Long planId) {
        return centralSupport.getMapperUtil().mapList(centralSupport.getEventRepository().findNotSomePlaceStatusEvents(planId, PlaceStatus.VOTING), eventMapper::mapEventToEventDTO);
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        return eventMapper.mapEventToEventDTO(checkService.checkEventExistence(eventId));
    }

    @Override
    @Transactional
    public EventDTO saveNewEvent(EventCreateDTO eventCreateDTO, Long travelPlanId, UUID userId) {
        if(eventCreateDTO.getStartTime().compareTo(eventCreateDTO.getEndTime()) > -1){
            throw new BadRequest("Invalid dates");
        }
        if (eventCreateDTO.getPlaceStatus() == PlaceStatus.CONCRETE && !centralSupport.getEventRepository().findEventsByTravelPlanAndTimeAndPlaceStatus(
                travelPlanId,
                PlaceStatus.CONCRETE,
                eventCreateDTO.getStartTime(),
                eventCreateDTO.getEndTime())
                .isEmpty()
        ) {
            throw new OverlappingEventsException();
        }
        Event newEvent = eventMapper.mapEventCreateDTOtoEvent(eventCreateDTO, travelPlanId, userId);
        newEvent = centralSupport.getEventRepository().save(newEvent);
        return eventMapper.mapEventToEventDTO(newEvent);
    }

    @Transactional
    @Override
    public void updateEvent(EventDTO eventDTO, Long travelPlanId, UUID userId) {
        Event event = checkService.checkEventExistence(eventDTO.getId());
        if(event.getPlaceStatus() == PlaceStatus.VOTING){
            throw new BadRequest("Event is voted, cannot update!");
        }
        if(event.getCreator().getId().equals(userId) || event.getTravelPlan().getOwner().getId().equals(userId)){
            if(eventDTO.getPlaceStatus() == PlaceStatus.CONCRETE) {
                centralSupport.getEventRepository().updateEventStatusByTravelPlanAndTime(
                        travelPlanId,
                        PlaceStatus.CONCRETE,
                        PlaceStatus.SUGGESTED,
                        eventDTO.getStartTime(),
                        eventDTO.getEndTime()
                );
                event.setPlaceStatus(PlaceStatus.CONCRETE);
            } else {
                event.setPlaceStatus(PlaceStatus.SUGGESTED);
            }
            event.setLocation(event.getLocation());
            event.setTitle(eventDTO.getTitle());
            event.setDescription(eventDTO.getDescription());
            event.setStartTime(eventDTO.getStartTime());
            event.setEndTime(eventDTO.getEndTime());
            event.setMaxCost(eventDTO.getMaxCost());
            event.setMinCost(eventDTO.getMinCost());
            centralSupport.getEventRepository().save(event);
        } else {
            throw new NoPermissionException();
        }

    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId, UUID userId) {
        Event event = checkService.checkEventExistence(eventId);
        if(event.getPlaceStatus() == PlaceStatus.VOTING){
            throw new BadRequest("Event is voted, cannot delete!");
        }
        if (event.getCreator().getId().equals(userId) || event.getTravelPlan().getOwner().getId().equals(userId)) {
            centralSupport.getEventRepository().deleteById(eventId);
        } else {
            throw new NoPermissionException("Not permitted to perform this operation!");
        }
    }

    @Override
    public List<EventDTO> getEventsByDay(String placeStatus, Long travelPlanId, LocalDate day) {
        LocalDateTime startOfTheDay = day.atStartOfDay();
        LocalDateTime endOfTheDay = LocalDateTime.of(day, LocalTime.MAX);
        return centralSupport.getMapperUtil().mapList(
                centralSupport.getEventRepository().findEventsByTravelPlanIdAndPlaceStatusAndDay(travelPlanId, PlaceStatus.valueOf(placeStatus), startOfTheDay, endOfTheDay),
                eventMapper::mapEventToEventDTO);
    }

    @Override
    public List<EventDTO> getEvents(String placeStatus, Long travelPlanId) {
        return centralSupport.getMapperUtil().mapList(
                centralSupport.getEventRepository().findEventsByTravelPlanIdAndPlaceStatus(travelPlanId, PlaceStatus.valueOf(placeStatus)),
                eventMapper::mapEventToEventDTO);
    }

}
