package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.travelplanning.dto.TravelPlanDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TravellingServiceTest {

    @Autowired
    TravellingService travellingService;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testSaveTravelPlan() {
        TravelPlanDTO travelPlanDTO = new TravelPlanDTO("Japan trip");
        TravelPlan newTravelPlan = travellingService.saveNewTravelPlan(travelPlanDTO, userRepository.findByUsername("Alext").get());

        assertThat(newTravelPlan).isNotNull();
        assertThat(newTravelPlan.getId()).isNotNull();


    }

}