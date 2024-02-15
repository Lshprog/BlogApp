package com.example.TravelPlanner.travelplanning.utils;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private final ModelMapper modelMapper;

    @Autowired
    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TravelPlan convertToTravelPlan(TravelPlanDTO travelPlanDTO) {
        TravelPlan travelPlan = modelMapper.map(travelPlanDTO, TravelPlan.class);
        return travelPlan;
    }


}
