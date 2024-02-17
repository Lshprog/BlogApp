package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.utils.CommonUtils;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
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
public class TravellingService implements TravelPlanService{

    private final EventRepository eventRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;

    @Override
    public List<TravelPlanDTO> listAllTravelPlansByUser(UUID userId) {
        return MapperUtil.mapList(travelPlanRepository.findTravelPlansByUserId(userId), TravelPlanDTO.class);
    }

    @Transactional
    @Override
    public TravelPlan saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, User user) {
        TravelPlan newTravelPlan = MapperUtil.map(travelPlanCreateDTO, TravelPlan.class);
        newTravelPlan.setOwner(user);
        newTravelPlan = travelPlanRepository.save(newTravelPlan);
        return newTravelPlan;
    }

    @Transactional
    @Override
    public void updateTravelPlan(TravelPlanDTO travelPlanDTO, User user) throws NoSuchElementException {
        Optional<TravelPlan> optionalTravelPlan = travelPlanRepository.findById(travelPlanDTO.getId());
        if (optionalTravelPlan.isEmpty()){
            // Create custom EntityNotFoundException
            throw new NoSuchElementException();
        }
        TravelPlan curTravelPlan = optionalTravelPlan.get();
        curTravelPlan.setTitle(travelPlanDTO.getTitle());
        curTravelPlan.setStartDate(travelPlanDTO.getStartDate());
        curTravelPlan.setEndDate(travelPlanDTO.getEndDate());
        travelPlanRepository.save(curTravelPlan);
    }

    @Transactional
    @Override
    public void delete(TravelPlanDTO travelPlanDTO, User user) {
        travelPlanRepository.deleteById(travelPlanDTO.getId());
    }

    @Transactional
    @Override
    public String generateNewInviteLink(TravelPlanDTO travelPlanDTO, User user) {
        String newInviteLink = CommonUtils.generateJoinLink();
        travelPlanRepository.updateJoinCodeTravelPlan(travelPlanDTO.getId(), newInviteLink);
        return newInviteLink;
    }

}
