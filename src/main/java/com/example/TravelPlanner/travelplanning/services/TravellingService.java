package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.repositories.PlaceRepository;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRolesRepository;
import com.example.TravelPlanner.travelplanning.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TravellingService implements TravelPlanService{

    private final PlaceRepository placeRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final UserPlanRolesRepository userPlanRolesRepository;
    private final MapperUtil mapperUtil;

    @Override
    public List<TravelPlan> listAllTravelPlansByUser(UUID userId) {
        return travelPlanRepository.findTravelPlansByUserId(userId);
    }

    @Transactional
    @Override
    public TravelPlan saveNewTravelPlan(TravelPlanDTO travelPlanDTO, User user) {
        TravelPlan newTravelPlan = mapperUtil.convertToTravelPlan(travelPlanDTO);
        newTravelPlan.setOwner(user);
        newTravelPlan = travelPlanRepository.save(newTravelPlan);
        return newTravelPlan;
    }


}
