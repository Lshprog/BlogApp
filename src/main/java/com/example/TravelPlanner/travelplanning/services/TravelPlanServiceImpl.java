package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.exceptions.custom.*;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EntityNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.UserNotFoundException;
import com.example.TravelPlanner.common.utils.mappers.CommonUtils;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.common.utils.mappers.traveplan.TravelPlanMapper;
import com.example.TravelPlanner.travelplanning.common.CheckService;
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
    private final CheckService checkService;

    @Override
    public List<TravelPlanPreviewDTO> listAllTravelPlansByUser(UUID userId) {
        Optional<User> optionalUser = centralSupport.getUserRepository().findById(userId);
        if(optionalUser.isEmpty()){
            throw new UserNotFoundException(userId);
        }
        return centralSupport.getMapperUtil().mapList(optionalUser.get().getTravelPlans(), TravelPlanPreviewDTO.class);
    }

    @Override
    public TravelPlanDTO getTravelPlanById(Long travelPlanId) {
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(centralSupport.getTravelPlanRepository().getReferenceById(travelPlanId));
    }

    @Override
    public List<String> findPlanUsers(Long travelPlanId) {
        return centralSupport.getUserPlanRepository().findUsersByTravelPlan(travelPlanId);
    }

    @Override
    @Transactional
    public TravelPlanDTO joinTravelPlan(String joinCode, UUID userId) {
        TravelPlan travelPlan = checkService.checkTravelPlanExistence(joinCode);
        User user = centralSupport.getUserRepository().getReferenceById(userId);
        if (!centralSupport.getUserPlanRepository().existsByUserIdAndTravelPlanId(user.getId(), travelPlan.getId())) {
            UserPlan userPlan = UserPlan.builder()
                    .travelPlan(travelPlan)
                    .user(user)
                    .build();
            centralSupport.getUserPlanRepository().save(userPlan);
        }
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(travelPlan);
    }

    @Transactional
    @Override
    public TravelPlanDTO saveNewTravelPlan(TravelPlanCreateDTO travelPlanCreateDTO, UUID userId) {
        TravelPlan newTravelPlan = travelPlanMapper.mapTravelPlanCreateDTOToTravelPlan(travelPlanCreateDTO, userId);
        newTravelPlan.setJoinCode(CommonUtils.generateJoinLink());
        newTravelPlan = centralSupport.getTravelPlanRepository().save(newTravelPlan);
        UserPlan userPlan = new UserPlan();
        userPlan.setTravelPlan(newTravelPlan);
        userPlan.setUser(centralSupport.getUserRepository().getReferenceById(userId));
        centralSupport.getUserPlanRepository().save(userPlan);
        return travelPlanMapper.mapTravelPlanToTravelPlanDTO(newTravelPlan);
    }

    @Transactional
    @Override
    public void updateTravelPlan(TravelPlanUpdateDTO travelPlanDTO, Long travelPlanId, UUID userId) {
        TravelPlan curTravelPlan = centralSupport.getTravelPlanRepository().findById(travelPlanId).get();
        curTravelPlan.setTitle(travelPlanDTO.getTitle());
        curTravelPlan.setStartDate(travelPlanDTO.getStartDate());
        curTravelPlan.setEndDate(travelPlanDTO.getEndDate());
        centralSupport.getTravelPlanRepository().save(curTravelPlan);
    }

    @Transactional
    @Override
    public void leaveTravelPlan(Long travelPlanId, UUID userId) {
        User user = centralSupport.getUserRepository().getReferenceById(userId);
        TravelPlan travelPlan = centralSupport.getTravelPlanRepository().findById(travelPlanId).get();
        if(Objects.equals(user.getUsername(), travelPlan.getOwner().getUsername())){
            List<UserPlan> userPlans = centralSupport.getUserPlanRepository().findUserPlansByTravelPlan(travelPlanId);
            if(userPlans.size() < 2){
                centralSupport.getTravelPlanRepository().delete(travelPlan);
                // maybe return here
            } else {
                 UserPlan curUserPlan = userPlans.get(1);
                 if(Objects.equals(curUserPlan.getUser().getUsername(), user.getUsername())) curUserPlan = userPlans.get(0);
                 travelPlan.setOwner(curUserPlan.getUser());
                 centralSupport.getTravelPlanRepository().save(travelPlan);
                 curUserPlan.getUser().getTravelPlans().add(travelPlan);
                 centralSupport.getUserRepository().save(curUserPlan.getUser());
            }
        }
        centralSupport.getUserPlanRepository().deleteByTravelPlanAndUser(travelPlan, user);
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
    public String generateNewInviteLink(Long travelPlanId, UUID userId) {
        String newInviteLink = CommonUtils.generateJoinLink();
        if(centralSupport.getTravelPlanRepository().getReferenceById(travelPlanId).getOwner().getId() == userId){
            centralSupport.getTravelPlanRepository().updateJoinCodeTravelPlan(travelPlanId, newInviteLink);
            return newInviteLink;
        } else {
            throw new NoPermissionException();
        }

    }

}
