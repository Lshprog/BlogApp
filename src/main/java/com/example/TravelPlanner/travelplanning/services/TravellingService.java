package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.*;
import com.example.TravelPlanner.common.utils.CommonUtils;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.event.EventMapper;
import com.example.TravelPlanner.common.utils.mappers.traveplan.TravelPlanMapper;
import com.example.TravelPlanner.common.utils.mappers.voting.VoteMapper;
import com.example.TravelPlanner.common.utils.mappers.voting.VotingMapper;
import com.example.TravelPlanner.travelplanning.common.enums.PlaceStatus;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import com.example.TravelPlanner.travelplanning.dto.event.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanPreviewDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanUpdateDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VoteDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingPreviewDTO;
import com.example.TravelPlanner.travelplanning.entities.*;
import com.example.TravelPlanner.travelplanning.repositories.*;
import com.example.TravelPlanner.common.utils.MapperUtil;
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
    private final VotesRepository votesRepository;
    private final MappingSupport mappingSupport;
    private final EventMapper eventMapper;
    private final VotingMapper votingMapper;
    private final TravelPlanMapper travelPlanMapper;

    @Override
    public List<TravelPlanPreviewDTO> listAllTravelPlansByUser(UUID userId) {
        return mappingSupport.getMapperUtil().mapList(travelPlanRepository.findTravelPlansByUserId(userId), TravelPlanPreviewDTO.class);
    }

    @Override
    public TravelPlanDTO getTravelPlanById(Long planId) {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(planId);
        if (optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(planId);
        }
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(optionalTravelPlan.get());
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
        UserPlanRoles userPlanRoles = UserPlanRoles.builder()
                .travelPlan(travelPlan)
                .role(PlanRole.EDITOR)
                .user(user)
                .build();
        userPlanRolesRepository.save(userPlanRoles);
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(travelPlan);
    }

    @Transactional
    @Override
    public TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = travelPlanMapper.mapTravelPlanCreateDTOToTravelPlan(travelPlanCreateDTO, userId);
        newTravelPlan.setJoinCode(CommonUtils.generateJoinLink());
        newTravelPlan = travelPlanRepository.save(newTravelPlan);
        UserPlanRoles userPlanRoles = UserPlanRoles.builder()
                .travelPlan(newTravelPlan)
                .role(PlanRole.OWNER)
                .user(userRepository.getReferenceById(userId))
                .build();
        userPlanRolesRepository.save(userPlanRoles);
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(newTravelPlan);
    }

    @Transactional
    @Override
    public void updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, UUID userId) throws NoSuchElementException {
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
        return mappingSupport.getMapperUtil().mapList(eventRepository.findNotSomePlaceStatusEvents(planId, PlaceStatus.VOTING), eventMapper::mapEventToEventDTO);
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()){
            throw new EventNotFoundException(eventId);
        }
        return eventMapper.mapEventToEventDTO(optionalEvent.get());
    }

    @Override
    @Transactional
    public EventDTO saveNewEvent(EventCreateDTO eventCreateDTO, UUID userId, Long travelPlanId) {
        Event newEvent = eventMapper.mapEventCreateDTOtoEvent(eventCreateDTO, travelPlanId, userId);
        newEvent = eventRepository.save(newEvent);
        return eventMapper.mapEventToEventDTO(newEvent);
    }

    @Transactional
    @Override
    public void updateEvent(EventDTO eventDTO, Long travelPlanId) {
        Optional<Event> eventOptional = eventRepository.findById(eventDTO.getId());
        if(eventOptional.isEmpty()){
            throw new EventNotFoundException(eventDTO.getId());
        }
        Event event = eventOptional.get();
        if(eventDTO.getPlaceStatus() == PlaceStatus.CONCRETE &&
                (event.getPlaceStatus() == PlaceStatus.SUGGESTED || event.getPlaceStatus() == PlaceStatus.VOTING)) {
            eventRepository.updateEventStatusByTravelPlanAndTime(
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
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    public VotingDTO getVotingById(Long votingId) {
        Optional<Voting> optionalVoting = votingRepository.findById(votingId);
        if (optionalVoting.isEmpty()) {
            throw new NoSuchElementException();
        }
        return votingMapper.mapVotingToVotingDto(optionalVoting.get());
    }

    @Override
    public void deleteVoting(Long votingId) {
        votingRepository.deleteById(votingId);
    }

    @Override
    @Transactional
    public VotingDTO createNewVoting(VotingCreateDTO votingCreateDTO, UUID userId) {
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
        Voting voting = votingMapper.mapVotingCreateDTOtoVoting(votingCreateDTO,userId);
        voting = votingRepository.save(voting);
        event.setPlaceStatus(PlaceStatus.VOTING);
        eventRepository.save(event);
        return votingMapper.mapVotingToVotingDto(voting);
    }

    @Override
    @Transactional
    public void makeVote(VoteDTO voteDTO, UUID user_id) {
        Vote vote = mappingSupport.getMapperUtil().map(voteDTO, Vote.class);
        User user = userRepository.getReferenceById(user_id);
        Voting voting = votingRepository.getReferenceById(voteDTO.getVotingId());
        if (votesRepository.findByCreatorAndVoting(user,voting) != null){
            throw new AlreadyVotedException(user.getUsername());
        }
        vote.setCreator(user);
        vote.setVoting(voting);
        votesRepository.save(vote);
    }

    @Override
    public List<VotingPreviewDTO> getVotingsByTravelPlan(Long travelPlanId) {
        return mappingSupport.getMapperUtil().mapList(votingRepository.findVotingsByTravelPlanId(travelPlanId), votingMapper::mapVotingToVotingPreviewDto);
    }

}
