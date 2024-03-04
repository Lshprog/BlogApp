package com.example.TravelPlanner.travelplanning.services;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.travelplanning.dto.travelplan.TravelPlanCreateDTO;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        LocalDate startDate = LocalDate.of(2023, 3, 8);
        LocalDate endDate = LocalDate.of(2023, 3, 12);
        TravelPlanCreateDTO travelPlanCreateDTO = new TravelPlanCreateDTO("Japan trip", startDate, endDate);
        TravelPlan newTravelPlan = travellingService.saveNewTravelPlan(travelPlanCreateDTO, userRepository.findByUsername("Alext").get());

        assertThat(newTravelPlan).isNotNull();
        assertThat(newTravelPlan.getId()).isNotNull();


    }

}