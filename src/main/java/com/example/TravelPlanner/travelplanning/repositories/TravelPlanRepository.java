package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    @Query("SELECT u.travelPlans FROM User u WHERE u.id = :userId")
    List<TravelPlan> findTravelPlansByUserId(UUID userId);

    @Query("SELECT t.joinCode FROM TravelPlan t WHERE t.id = :planId")
    String getJoinCodeByTravelPlanId(Long planId);

}
