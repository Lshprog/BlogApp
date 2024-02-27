package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.EventNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.CommonUtils;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import com.example.TravelPlanner.travelplanning.dto.*;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import com.example.TravelPlanner.travelplanning.repositories.EventRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRolesRepository;
import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.travelplanning.repositories.VotingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TravellingService implements TravelPlanService, EventService, VotingService{

    private final EventRepository eventRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;
    private final UserRepository userRepository;
    private final VotingRepository votingRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<TravelPlanDTO> listAllTravelPlansByUser(UUID userId) {
        return mapperUtil.mapList(travelPlanRepository.findTravelPlansByUserId(userId), TravelPlanDTO.class);
    }

    @Override
    public TravelPlanDTO getTravelPlanById(Long planId) {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(planId);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(planId);
        }
        return mapperUtil.map(optionalTravelPlan.get(), TravelPlanDTO.class);
    }

    @Override
    public List<UserPlanRoles> findPlanUsers(TravelPlanDTO travelPlanDTO) {
        return userPlanRolesRepository.findUserPlanRolesByTravelPlan(travelPlanDTO.getId());
    }

    @Transactional
    @Override
    public TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = mapperUtil.map(travelPlanCreateDTO, TravelPlan.class);
        User user = userRepository.getReferenceById(userId);
        newTravelPlan.setOwner(user);
        newTravelPlan = travelPlanRepository.save(newTravelPlan);
        return mapperUtil.map(newTravelPlan, TravelPlanDTO.class);
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
    public void leaveTravelPlan(TravelPlanDTO travelPlanDTO, UUID userId) {
        User user = userRepository.getReferenceById(userId);
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(travelPlanDTO.getId());
        if(optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(travelPlanDTO.getId());
        }
        TravelPlan travelPlan = optionalTravelPlan.get();
        PlanRole planRole = userPlanRolesRepository.findUserPlanRoleByUserAndTravelPlan(user, travelPlan);
        if(planRole == PlanRole.OWNER){
            Optional<UserPlanRoles> optionalUserPlanRoles = userPlanRolesRepository.findUserPlanRolesByTravelPlanAndRole(travelPlan, PlanRole.EDITOR);
            if(optionalUserPlanRoles.isEmpty()){
                travelPlanRepository.delete(travelPlan);
            } else {
                UserPlanRoles userPlanRoles = optionalUserPlanRoles.get();
                userPlanRoles.setRole(PlanRole.OWNER);
                userPlanRolesRepository.save(userPlanRoles);
            }
        } else {
            userPlanRolesRepository.deleteByTravelPlanAndUser(travelPlan, user);
        }

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
        return mapperUtil.mapList(eventRepository.findEventsByTravelPlanId(planId), EventDTO.class);
    }

    private void setEventLocation(Event curEvent, Location newLocation) {
        curEvent.setLoc(newLocation);
        curEvent.setLocation(mapperUtil.convertPojoToJson(newLocation));
    }

    private Location getEventLocation(Event curEvent) {
        if (curEvent.getLoc() == null && curEvent.getLocation() != null) {
            curEvent.setLoc(mapperUtil.convertJsonToPojo(curEvent.getLocation(), Location.class));
        }
        return curEvent.getLoc();
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()){
            throw new EventNotFoundException(eventId);
        }
        return mapperUtil.map(optionalEvent.get(), EventDTO.class);
    }

    @Override
    @Transactional
    public EventDTO saveNewEvent(EventCreateDTO eventCreateDTO, Long planId, UUID userId) {
        Event newEvent = mapperUtil.map(eventCreateDTO, Event.class);
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(planId);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(planId);
        }
        newEvent.setTravelPlan(optionalTravelPlan.get());
        setEventLocation(newEvent, eventCreateDTO.getLoc());
        newEvent.setCreator(userRepository.findById(userId).get());
        return mapperUtil.map(eventRepository.save(newEvent), EventDTO.class);


    }

    @Transactional
    @Override
    public void updateEvent(EventDTO eventDTO) {
        Event event = eventRepository.getReferenceById(eventDTO.getId());
        if(eventDTO.getPlaceStatus() == PlaceStatus.CONCRETE &&
                (event.getPlaceStatus() == PlaceStatus.SUGGESTED || event.getPlaceStatus() == PlaceStatus.VOTING)) {
            eventRepository.updateEventStatusByTravelPlanAndTime(
                    eventDTO.getTravelPlanId(),
                    event.getPlaceStatus(),
                    event.getPlaceStatus(),
                    event.getStartTime(),
                    event.getEndTime()
                    );
            event.setPlaceStatus(PlaceStatus.CONCRETE);
            eventRepository.save(event);
        } else {
            event.setPlaceStatus(PlaceStatus.SUGGESTED);
            eventRepository.save(event);
        }
    }

    @Override
    @Transactional
    public void delete(EventDTO eventDTO) {
        eventRepository.deleteById(eventDTO.getId());
    }

    @Override
    @Transactional
    public VotingDTO createNewVoting(VotingDTO votingDTO) {
        EventDTO eventDTO = votingDTO.getEvent();
        List<Event> overlappingEvents = eventRepository.findEventsByTravelPlanAndTimeAndPlaceStatus(
                eventDTO.getTravelPlanId(),
                PlaceStatus.VOTING,
                eventDTO.getStartTime(),
                eventDTO.getStartTime()
        );
        if (!overlappingEvents.isEmpty()){
            // throw new exception
        }
        Voting voting = mapperUtil.map(votingDTO, Voting.class);
        voting.setEvent(eventRepository.getReferenceById(eventDTO.getId()));
        //voting.setCreator(userRepository.findByUsername(votingDTO.getCreator()).get());
        return mapperUtil.map(votingRepository.save(voting),VotingDTO.class);
    }

    @Override
    @Transactional
    public void makeVote(Long votingId) {
        
    }
}
