package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.*;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EntityNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.utils.mappers.CommonUtils;
import com.example.TravelPlanner.common.utils.CentralSupport;
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

    private final CentralSupport centralSupport;
    private final TravelPlanMapper travelPlanMapper;

    @Override
    public List<TravelPlanPreviewDTO> listAllTravelPlansByUser(UUID userId) {
        return centralSupport.getMapperUtil().mapList(centralSupport.getUserPlanRolesRepository().findTravelPlansByUserId(userId), TravelPlanPreviewDTO.class);
    }

    @Override
    public TravelPlanDTO getTravelPlanById(Long travelPlanId) {
        Optional<TravelPlan> optionalTravelPlan = centralSupport.getTravelPlanRepository().findById(travelPlanId);
        if(optionalTravelPlan.isEmpty()) {
            throw new TravelPlanNotFoundException(travelPlanId);
        }
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(optionalTravelPlan.get());
    }

    @Override
    public List<UserPlanRoles> findPlanUsers(Long travelPlanId) {
        List<UserPlanRoles> planRoles = new ArrayList<>();
        try{
            planRoles = centralSupport.getUserPlanRolesRepository().findUserPlanRolesByTravelPlan(travelPlanId);
        } catch (EntityNotFoundException e) {
            throw new TravelPlanNotFoundException(travelPlanId);
        }
        return planRoles;
    }

    @Override
    @Transactional
    public TravelPlanDTO joinTravelPlan(String joinCode, UUID userId) {
        Optional<TravelPlan> optionalTravelPlan = centralSupport.getTravelPlanRepository().getTravelPlanByJoinCode(joinCode);
        if (optionalTravelPlan.isEmpty()) {
            throw new TravelPlanNotFoundException(joinCode);
        }
        TravelPlan travelPlan = optionalTravelPlan.get();
        User user = centralSupport.getUserRepository().getReferenceById(userId);
        if (!centralSupport.getUserPlanRolesRepository().existsByUserIdAndTravelPlanId(user.getId(), travelPlan.getId())) {
            UserPlanRoles userPlanRoles = UserPlanRoles.builder()
                    .travelPlan(travelPlan)
                    .role(PlanRole.EDITOR)
                    .user(user)
                    .build();
            centralSupport.getUserPlanRolesRepository().save(userPlanRoles);
        }
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(travelPlan);
    }

    @Transactional
    @Override
    public TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = travelPlanMapper.mapTravelPlanCreateDTOToTravelPlan(travelPlanCreateDTO, userId);
        newTravelPlan.setJoinCode(CommonUtils.generateJoinLink());
        newTravelPlan = centralSupport.getTravelPlanRepository().save(newTravelPlan);
        UserPlanRoles userPlanRoles = UserPlanRoles.builder()
                .travelPlan(newTravelPlan)
                .role(PlanRole.OWNER)
                .user(centralSupport.getUserRepository().getReferenceById(userId))
                .build();
        centralSupport.getUserPlanRolesRepository().save(userPlanRoles);
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(newTravelPlan);
    }

    @Transactional
    @Override
    public void updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, Long travelPlanId, UUID userId) {
        Optional<TravelPlan> optionalTravelPlan = centralSupport.getTravelPlanRepository().findById(travelPlanId);
        if(optionalTravelPlan.isEmpty()) {
            throw new TravelPlanNotFoundException(travelPlanId);
        }
        TravelPlan curTravelPlan = optionalTravelPlan.get();
        curTravelPlan.setTitle(travelPlanDTO.getTitle());
        curTravelPlan.setStartDate(travelPlanDTO.getStartDate());
        curTravelPlan.setEndDate(travelPlanDTO.getEndDate());
        centralSupport.getTravelPlanRepository().save(curTravelPlan);
    }

    @Transactional
    @Override
    public void leaveTravelPlan(Long travelPlanId, UUID userId) {
        User user = centralSupport.getUserRepository().getReferenceById(userId);
        TravelPlan travelPlan = centralSupport.getTravelPlanRepository().getReferenceById(travelPlanId);
        PlanRole planRole = centralSupport.getUserPlanRolesRepository().findUserPlanRoleByUserAndTravelPlan(user, travelPlan).get();
        if(planRole == PlanRole.OWNER){
            Optional<UserPlanRoles> optionalUserPlanRoles = centralSupport.getUserPlanRolesRepository().findUserPlanRolesByTravelPlanAndRole(travelPlan, PlanRole.EDITOR);
            if(optionalUserPlanRoles.isEmpty()){
                centralSupport.getTravelPlanRepository().delete(travelPlan);
            } else {
                UserPlanRoles userPlanRoles = optionalUserPlanRoles.get();
                userPlanRoles.setRole(PlanRole.OWNER);
                centralSupport.getUserPlanRolesRepository().save(userPlanRoles);
            }
        } else {
            centralSupport.getUserPlanRolesRepository().deleteByTravelPlanAndUser(travelPlan, user);
        }

    }

    @Transactional
    @Override
    public void deleteTravelPlan(Long travelPlanId, UUID userId) {
        User user = centralSupport.getUserRepository().findById(userId).get();
        TravelPlan travelPlan = centralSupport.getTravelPlanRepository().findById(travelPlanId).get();
        if(travelPlan.getOwner().getId() != userId){
            throw new NotOwnerException();
        }
        centralSupport.getTravelPlanRepository().deleteById(travelPlanId);
    }

    @Transactional
    @Override
    public String generateNewInviteLink(Long travelPlanId) {
        String newInviteLink = CommonUtils.generateJoinLink();
        centralSupport.getTravelPlanRepository().updateJoinCodeTravelPlan(travelPlanId, newInviteLink);
        return newInviteLink;
    }

}
