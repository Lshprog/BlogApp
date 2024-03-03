package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.*;
import com.example.TravelPlanner.common.utils.CommonUtils;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import com.example.TravelPlanner.travelplanning.common.pojos.Location;
import com.example.TravelPlanner.travelplanning.dto.*;
import com.example.TravelPlanner.travelplanning.entities.*;
import com.example.TravelPlanner.travelplanning.repositories.*;
import com.example.TravelPlanner.common.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final VotesRepository votesRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<TravelPlanShowDTO> listAllTravelPlansByUser(UUID userId) {
        return mapperUtil.mapList(travelPlanRepository.findTravelPlansByUserId(userId), TravelPlanShowDTO.class);
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

    @Override
    @Transactional
    public TravelPlanDTO joinTravelPlan(String joinCode, UUID userId) {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.getTravelPlanByJoinCode(joinCode);
        if(optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(joinCode);
        }
        TravelPlan travelPlan = optionalTravelPlan.get();
        User user = userRepository.getReferenceById(userId);
        UserPlanRoles userPlanRoles = new UserPlanRoles();
        userPlanRoles.setTravelPlan(travelPlan);
        userPlanRoles.setUser(user);
        userPlanRoles.setRole(PlanRole.EDITOR);
        userPlanRolesRepository.save(userPlanRoles);
        return mapperUtil.map(travelPlan, TravelPlanDTO.class);
    }

    @Transactional
    @Override
    public TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = mapperUtil.map(travelPlanCreateDTO, TravelPlan.class);
        User user = userRepository.getReferenceById(userId);
        newTravelPlan.setOwner(user);
        newTravelPlan.setJoinCode(CommonUtils.generateJoinLink());
        newTravelPlan = travelPlanRepository.save(newTravelPlan);
        UserPlanRoles userPlanRoles = new UserPlanRoles();
        userPlanRoles.setTravelPlan(newTravelPlan);
        userPlanRoles.setUser(user);
        userPlanRoles.setRole(PlanRole.OWNER);
        return mapperUtil.map(newTravelPlan, TravelPlanDTO.class);
    }

    @Transactional
    @Override
    public TravelPlanDTO updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, UUID userId) throws NoSuchElementException {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(travelPlanDTO.getId());
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(travelPlanDTO.getId());
        }
        TravelPlan curTravelPlan = optionalTravelPlan.get();
        curTravelPlan.setTitle(travelPlanDTO.getTitle());
        curTravelPlan.setStartDate(travelPlanDTO.getStartDate());
        curTravelPlan.setEndDate(travelPlanDTO.getEndDate());
        return mapperUtil.map(travelPlanRepository.save(curTravelPlan), TravelPlanDTO.class);
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
        PlanRole planRole = userPlanRolesRepository.findUserPlanRoleByUserAndTravelPlan(user, travelPlan).get();
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
    public void deleteTravelPlan(Long travelPlanId, UUID userId) {
        User user = userRepository.getReferenceById(userId);
        TravelPlan travelPlan = travelPlanRepository.findById(travelPlanId).get();
        if(!user.getTravelPlans().contains(travelPlan)){
            throw new NotOwnerException();
        }
        travelPlanRepository.deleteById(travelPlanId);
    }

    @Transactional
    @Override
    public String generateNewInviteLink(Long travelPlanId) {
        String newInviteLink = CommonUtils.generateJoinLink();
        travelPlanRepository.updateJoinCodeTravelPlan(travelPlanId, newInviteLink);
        return newInviteLink;
    }

    @Override
    public List<EventDTO> listAllEventsByTravelPlan(Long planId) {
        return mapperUtil.mapList(eventRepository.findEventsByTravelPlanId(planId), EventDTO.class);
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
    public EventDTO saveNewEvent(EventCreateDTO eventCreateDTO, UUID userId) {
        Event newEvent = mapperUtil.map(eventCreateDTO, Event.class);
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(eventCreateDTO.getTravelPlanId());
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(eventCreateDTO.getTravelPlanId());
        }
        newEvent.setTravelPlan(optionalTravelPlan.get());
        setEventLocation(newEvent, eventCreateDTO.getLoc());
        newEvent.setCreator(userRepository.getReferenceById(userId));
        newEvent = eventRepository.save(newEvent);
        newEvent.setLoc(eventCreateDTO.getLoc());
        return mapperUtil.map(newEvent, EventDTO.class);
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

    @Transactional
    @Override
    public EventDTO updateEvent(EventDTO eventDTO) {
        Optional<Event> eventOptional = eventRepository.findById(eventDTO.getId());
        if(eventOptional.isEmpty()){
            throw new EventNotFoundException(eventDTO.getId());
        }
        Event event = eventOptional.get();
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

        } else {
            event.setPlaceStatus(PlaceStatus.SUGGESTED);
        }
        setEventLocation(event, eventDTO.getLoc());
        if(eventDTO.getVoting() != null){
            event.setVoting(votingRepository.getReferenceById(eventDTO.getVoting().getId()));
        }
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setStartTime(eventDTO.getStartTime());
        // here add check and except incorrect timeline
        event.setEndTime(eventDTO.getEndTime());
        return mapperUtil.map(eventRepository.save(event), EventDTO.class);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public VotingDTO getVotingById(Long votingId) {
        // excep
        return mapperUtil.map(votingRepository.findById(votingId).get(), VotingDTO.class);
    }

    @Override
    public void deleteVoting(Long votingId) {
        votingRepository.deleteById(votingId);
    }

    @Override
    @Transactional
    public VotingDTO createNewVoting(VotingCreateDTO votingCreateDTO, UUID user_id) {
        Optional<Event> optionalEvent = eventRepository.findById(votingCreateDTO.getEventId());
        if(optionalEvent.isEmpty()){
            throw new EventNotFoundException(votingCreateDTO.getEventId());
        }
        Event event = optionalEvent.get();
        List<Event> overlappingEvents = eventRepository.findEventsByTravelPlanAndTimeAndPlaceStatus(
                event.getTravelPlan().getId(),
                PlaceStatus.VOTING,
                event.getStartTime(),
                event.getStartTime()
        );
        if (!overlappingEvents.isEmpty()){
            // throw new exception
        }
        Voting voting = mapperUtil.map(votingCreateDTO, Voting.class);
        voting.setCreator(userRepository.findByUsername(votingCreateDTO.getCreator()).get());
        voting = votingRepository.save(voting);
        event.setVoting(voting);
        event.setPlaceStatus(PlaceStatus.VOTING);
        eventRepository.save(event);
        return mapperUtil.map(voting, VotingDTO.class);
    }

    @Override
    @Transactional
    public void makeVote(VoteDTO voteDTO, UUID user_id) {
        Vote vote = mapperUtil.map(voteDTO, Vote.class);
        User user = userRepository.getReferenceById(user_id);
        Voting voting = votingRepository.getReferenceById(voteDTO.getVoting_id());
        if (votesRepository.findByCreatorAndVoting(user,voting) != null){
            throw new AlreadyVotedException(user.getUsername());
        }
        vote.setCreator(user);
        vote.setVoting(voting);
        votesRepository.save(vote);
    }

}
