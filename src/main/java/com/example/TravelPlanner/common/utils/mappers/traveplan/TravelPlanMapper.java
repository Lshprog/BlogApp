package com.example.TravelPlanner.common.utils.mappers.traveplan;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.common.utils.MappingSupport;
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


    private final MappingSupport mappingSupport;
    private final EventMapper eventMapper;

    public TravelPlanDTO mapTravelPlanToTravelPlanDTO(TravelPlan travelPlan) {
        TravelPlanDTO travelPlanDTO = mappingSupport.getMapperUtil().map(travelPlan, TravelPlanDTO.class);
        travelPlanDTO.setEvents(mappingSupport.getMapperUtil().mapList(travelPlan.getEvents(), eventMapper::mapEventToEventDTO));
        return travelPlanDTO;
    }

    public TravelPlanPreviewDTO mapTravelPlanToTravelPlanPreviewDTO(TravelPlan travelPlan) {
        return mappingSupport.getMapperUtil().map(travelPlan, TravelPlanPreviewDTO.class);
    }

    public TravelPlan mapTravelPlanDTOToTravelPlan(TravelPlanDTO travelPlanDTO) {
        TravelPlan travelPlan = mappingSupport.getMapperUtil().map(travelPlanDTO, TravelPlan.class);
        travelPlan.setEvents(mappingSupport.getMapperUtil().mapList(travelPlanDTO.getEvents(), eventDTO ->  eventMapper.mapEventDTOToEvent(eventDTO, travelPlanDTO.getId())));
        travelPlan.setOwner(mappingSupport.getUserRepository().findByUsername(travelPlanDTO.getCreator())
                .orElseThrow(() -> new UserNotFoundException(travelPlanDTO.getCreator())));
        return travelPlan;
    }

    public TravelPlan mapTravelPlanCreateDTOToTravelPlan(TravelPlanCreateDTO travelPlanDTO, UUID userId) {
        TravelPlan travelPlan = mappingSupport.getMapperUtil().map(travelPlanDTO, TravelPlan.class);
        travelPlan.setOwner(mappingSupport.getUserRepository().getReferenceById(userId));
        return travelPlan;
    }
}