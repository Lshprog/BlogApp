package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.EventNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.CommonUtils;
import com.example.TravelPlanner.travelplanning.dto.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.EventDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRolesRepository;
import com.example.TravelPlanner.common.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TravellingService implements TravelPlanService, EventService{

    private final EventRepository eventRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;
    private final UserRepository userRepository;

    @Override
    public List<TravelPlanDTO> listAllTravelPlansByUser(UUID userId) {
        return MapperUtil.mapList(travelPlanRepository.findTravelPlansByUserId(userId), TravelPlanDTO.class);
    }

    @Override
    public TravelPlan getTravelPlanById(Long planId) {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(planId);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(planId);
        }
        return optionalTravelPlan.get();
    }

    @Transactional
    @Override
    public TravelPlan saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = MapperUtil.map(travelPlanCreateDTO, TravelPlan.class);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        newTravelPlan.setOwner(optionalUser.get());
        newTravelPlan = travelPlanRepository.save(newTravelPlan);
        return newTravelPlan;
    }

    @Transactional
    @Override
    public void updateTravelPlan(TravelPlanDTO travelPlanDTO) throws NoSuchElementException {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(travelPlanDTO.getId());
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(travelPlanDTO.getId());
        }
        TravelPlan curTravelPlan = optionalTravelPlan.get();
        curTravelPlan.setTitle(travelPlanDTO.getTitle());
        curTravelPlan.setStartDate(travelPlanDTO.getStartDate());
        curTravelPlan.setEndDate(travelPlanDTO.getEndDate());
        travelPlanRepository.save(curTravelPlan);
    }

    @Transactional
    @Override
    public void delete(TravelPlanDTO travelPlanDTO) {
        travelPlanRepository.deleteById(travelPlanDTO.getId());
    }

    @Transactional
    @Override
    public String generateNewInviteLink(TravelPlanDTO travelPlanDTO) {
        String newInviteLink = CommonUtils.generateJoinLink();
        travelPlanRepository.updateJoinCodeTravelPlan(travelPlanDTO.getId(), newInviteLink);
        return newInviteLink;
    }

    @Override
    public List<EventDTO> listAllEventsByTravelPlan(Long planId) {
        return MapperUtil.mapList(eventRepository.findEventsByTravelPlanId(planId), EventDTO.class);
    }

    @Override
    public Event getEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()){
            throw new EventNotFoundException(eventId);
        }
        return optionalEvent.get();
    }

    @Override
    public Event saveNewEvent(EventCreateDTO eventCreateDTO, Long planId) {
        Event newEvent = MapperUtil.map(eventCreateDTO, Event.class);
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(planId);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(planId);
        }
        newEvent.setTravelPlan(optionalTravelPlan.get());
        newEvent.setEventLocation(eventCreateDTO.getLoc());
        newEvent = eventRepository.save(newEvent);
        return newEvent;
    }

    @Override
    public void updateEvent(EventDTO eventDTO) {
        // do i need to check here?
    }

    @Override
    public void delete(EventDTO eventDTO) {
        eventRepository.deleteById(eventDTO.getId());
    }
}
