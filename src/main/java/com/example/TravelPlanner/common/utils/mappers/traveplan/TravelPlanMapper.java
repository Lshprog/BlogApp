package com.example.TravelPlanner.common.utils.mappers.traveplan;

import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.UserNotFoundException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.example.TravelPlanner.common.utils.mappers.event.EventMapper;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanPreviewDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TravelPlanMapper {


    private final CentralSupport centralSupport;
    private final EventMapper eventMapper;

    public TravelPlanDTO mapTravelPlanToTravelPlanDTO(TravelPlan travelPlan) {
        TravelPlanDTO travelPlanDTO = centralSupport.getMapperUtil().map(travelPlan, TravelPlanDTO.class);
        travelPlanDTO.setEvents(centralSupport.getMapperUtil().mapList(travelPlan.getEvents(), eventMapper::mapEventToEventDTO));
        travelPlanDTO.setCreator(travelPlan.getOwner().getUsername());
        travelPlanDTO.setUsers(centralSupport.getUserPlanRepository().findUsersByTravelPlan(travelPlan.getId()));
        return travelPlanDTO;
    }

    public TravelPlanPreviewDTO mapTravelPlanToTravelPlanPreviewDTO(TravelPlan travelPlan) {
        return centralSupport.getMapperUtil().map(travelPlan, TravelPlanPreviewDTO.class);
    }

    public TravelPlan mapTravelPlanDTOToTravelPlan(TravelPlanDTO travelPlanDTO) {
        TravelPlan travelPlan = centralSupport.getMapperUtil().map(travelPlanDTO, TravelPlan.class);
        travelPlan.setEvents(centralSupport.getMapperUtil().mapList(travelPlanDTO.getEvents(), eventDTO ->  eventMapper.mapEventDTOToEvent(eventDTO, travelPlanDTO.getId())));
        travelPlan.setOwner(centralSupport.getUserRepository().findByUsername(travelPlanDTO.getCreator())
                .orElseThrow(() -> new UserNotFoundException(travelPlanDTO.getCreator())));
        return travelPlan;
    }

    public TravelPlan mapTravelPlanCreateDTOToTravelPlan(TravelPlanCreateDTO travelPlanDTO, UUID userId) {
        TravelPlan travelPlan = centralSupport.getMapperUtil().map(travelPlanDTO, TravelPlan.class);
        travelPlan.setOwner(centralSupport.getUserRepository().getReferenceById(userId));
        return travelPlan;
    }
}
