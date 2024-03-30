package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    @Query("SELECT u.travelPlans FROM User u WHERE u.id = :userId")
    List<TravelPlan> findTravelPlansByUserId(UUID userId);

    @Query("SELECT t.joinCode FROM TravelPlan t WHERE t.id = :planId")
    String getJoinCodeByTravelPlanId(Long planId);

    @Modifying
    @Query("UPDATE TravelPlan t SET t.joinCode = :joinCode WHERE t.id = :id")
    void updateJoinCodeTravelPlan(@Param(value = "id") Long id, @Param(value = "joinCode") String joinCode);

    Optional<TravelPlan> getTravelPlanByJoinCode(String joinCode);

}
