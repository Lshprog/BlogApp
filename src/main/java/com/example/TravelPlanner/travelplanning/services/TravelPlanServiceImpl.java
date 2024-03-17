package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.*;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EntityNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.utils.mappers.CommonUtils;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.traveplan.TravelPlanMapper;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanPreviewDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanUpdateDTO;
import com.example.TravelPlanner.travelplanning.entities.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class TravelPlanServiceImpl implements TravelPlanService{

    private final MappingSupport mappingSupport;
    private final TravelPlanMapper travelPlanMapper;

    @Override
    public List<TravelPlanPreviewDTO> listAllTravelPlansByUser(UUID userId) {
        return mappingSupport.getMapperUtil().mapList(mappingSupport.getTravelPlanRepository().findTravelPlansByUserId(userId), TravelPlanPreviewDTO.class);
    }

    @Override
    public TravelPlanDTO getTravelPlanById(Long travelPlanId) {
        Optional<TravelPlan> optionalTravelPlan = mappingSupport.getTravelPlanRepository().findById(travelPlanId);
        if(optionalTravelPlan.isEmpty()) {
            throw new TravelPlanNotFoundException(travelPlanId);
        }
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(optionalTravelPlan.get());
    }

    @Override
    public List<UserPlanRoles> findPlanUsers(Long travelPlanId) {
        List<UserPlanRoles> planRoles = new ArrayList<>();
        try{
            planRoles = mappingSupport.getUserPlanRolesRepository().findUserPlanRolesByTravelPlan(travelPlanId);
        } catch (EntityNotFoundException e) {
            throw new TravelPlanNotFoundException(travelPlanId);
        }
        return planRoles;
    }

    @Override
    @Transactional
    public TravelPlanDTO joinTravelPlan(String joinCode, UUID userId) {
        Optional<TravelPlan> optionalTravelPlan = mappingSupport.getTravelPlanRepository().getTravelPlanByJoinCode(joinCode);
        if(optionalTravelPlan.isEmpty()){
            throw new TravelPlanNotFoundException(joinCode);
        }
        TravelPlan travelPlan = optionalTravelPlan.get();
        User user = mappingSupport.getUserRepository().getReferenceById(userId);
        UserPlanRoles userPlanRoles = UserPlanRoles.builder()
                .travelPlan(travelPlan)
                .role(PlanRole.EDITOR)
                .user(user)
                .build();
        mappingSupport.getUserPlanRolesRepository().save(userPlanRoles);
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(travelPlan);
    }

    @Transactional
    @Override
    public TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = travelPlanMapper.mapTravelPlanCreateDTOToTravelPlan(travelPlanCreateDTO, userId);
        newTravelPlan.setJoinCode(CommonUtils.generateJoinLink());
        newTravelPlan = mappingSupport.getTravelPlanRepository().save(newTravelPlan);
        UserPlanRoles userPlanRoles = UserPlanRoles.builder()
                .travelPlan(newTravelPlan)
                .role(PlanRole.OWNER)
                .user(mappingSupport.getUserRepository().getReferenceById(userId))
                .build();
        mappingSupport.getUserPlanRolesRepository().save(userPlanRoles);
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(newTravelPlan);
    }

    @Transactional
    @Override
    public void updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, Long travelPlanId, UUID userId) {
        Optional<TravelPlan> optionalTravelPlan = mappingSupport.getTravelPlanRepository().findById(travelPlanId);
        if(optionalTravelPlan.isEmpty()) {
            throw new TravelPlanNotFoundException(travelPlanId);
        }
        TravelPlan curTravelPlan = optionalTravelPlan.get();
        curTravelPlan.setTitle(travelPlanDTO.getTitle());
        curTravelPlan.setStartDate(travelPlanDTO.getStartDate());
        curTravelPlan.setEndDate(travelPlanDTO.getEndDate());
        mappingSupport.getTravelPlanRepository().save(curTravelPlan);
    }

    @Transactional
    @Override
    public void leaveTravelPlan(Long travelPlanId, UUID userId) {
        User user = mappingSupport.getUserRepository().getReferenceById(userId);
        TravelPlan travelPlan = mappingSupport.getTravelPlanRepository().getReferenceById(travelPlanId);
        PlanRole planRole = mappingSupport.getUserPlanRolesRepository().findUserPlanRoleByUserAndTravelPlan(user, travelPlan).get();
        if(planRole == PlanRole.OWNER){
            Optional<UserPlanRoles> optionalUserPlanRoles = mappingSupport.getUserPlanRolesRepository().findUserPlanRolesByTravelPlanAndRole(travelPlan, PlanRole.EDITOR);
            if(optionalUserPlanRoles.isEmpty()){
                mappingSupport.getTravelPlanRepository().delete(travelPlan);
            } else {
                UserPlanRoles userPlanRoles = optionalUserPlanRoles.get();
                userPlanRoles.setRole(PlanRole.OWNER);
                mappingSupport.getUserPlanRolesRepository().save(userPlanRoles);
            }
        } else {
            mappingSupport.getUserPlanRolesRepository().deleteByTravelPlanAndUser(travelPlan, user);
        }

    }

    @Transactional
    @Override
    public void deleteTravelPlan(Long travelPlanId, UUID userId) {
        User user = mappingSupport.getUserRepository().findById(userId).get();
        TravelPlan travelPlan = mappingSupport.getTravelPlanRepository().findById(travelPlanId).get();
        if(!user.getTravelPlans().contains(travelPlan)){
            throw new NotOwnerException();
        }
        mappingSupport.getTravelPlanRepository().deleteById(travelPlanId);
    }

    @Transactional
    @Override
    public String generateNewInviteLink(Long travelPlanId) {
        String newInviteLink = CommonUtils.generateJoinLink();
        mappingSupport.getTravelPlanRepository().updateJoinCodeTravelPlan(travelPlanId, newInviteLink);
        return newInviteLink;
    }

}
