package com.example.TravelPlanner.common.utils.mappers.traveplan;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.common.exceptions.custom.UserNotFoundException;
import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.common.utils.MappingSupport;
import com.example.TravelPlanner.common.utils.mappers.CustomMapper;
import com.example.TravelPlanner.common.utils.mappers.event.AbstractEventMapper;
import com.example.TravelPlanner.common.utils.mappers.event.EventMapper;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TravelPlanMapper implements CustomMapper<TravelPlan, TravelPlanDTO> {


    private final MappingSupport mappingSupport;
    private final MapperUtil mapperUtil = mappingSupport.getMapperUtil();
    private final EventMapper eventMapper;
    private final UserRepository userRepository = mappingSupport.getUserRepository();

    @Override
    public TravelPlanDTO mapToDTO(TravelPlan travelPlan) {
        TravelPlanDTO travelPlanDTO = mapperUtil.map(travelPlan, TravelPlanDTO.class);
        travelPlanDTO.setEvents(mapperUtil.mapList(travelPlan.getEvents(), eventMapper::mapToDTO));
        return travelPlanDTO;
    }

    @Override
    public TravelPlan mapToEntity(TravelPlanDTO travelPlanDTO) {
        TravelPlan travelPlan = mapperUtil.map(travelPlanDTO, TravelPlan.class);
        travelPlan.setEvents(mapperUtil.mapList(travelPlanDTO.getEvents(), eventMapper::mapToEntity));
        travelPlan.setOwner(userRepository.findByUsername(travelPlanDTO.getOwnerUsername())
                .orElseThrow(() -> new UserNotFoundException(travelPlanDTO.getOwnerUsername())));
        return travelPlan;
    }
}
