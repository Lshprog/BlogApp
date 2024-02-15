package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TravelPlanRepositoryTest {

    @Autowired
    TravelPlanRepository travelPlanRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    public void testSaveTravelPlan() {

        TravelPlan travelPlan = travelPlanRepository.save(TravelPlan.builder()
                .owner(userRepository.findByUsername("Alext").get())
                .title("Tokyo whatever")
                .build());

        assertThat(travelPlan).isNotNull();
        assertThat(travelPlan.getId()).isNotNull();


    }

}