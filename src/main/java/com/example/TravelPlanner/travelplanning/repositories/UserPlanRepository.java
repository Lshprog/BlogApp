package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Long> {

    @Query("SELECT upr.travelPlan FROM UserPlan upr WHERE upr.user.id = :userId")
    List<TravelPlan> findTravelPlansByUserId(@Param(value = "userId") UUID userId);

    @Query("SELECT upr FROM UserPlan upr WHERE upr.travelPlan.id = :travelPlanId AND upr.user.id = :userId")
    UserPlan getUserPlanByUserIdAndTravelPlanId(@Param(value = "travelPlanId") Long travelPlanId,
                                                         @Param(value = "userId") UUID userId);

    @Query("SELECT CASE WHEN COUNT(upr) > 0 THEN true ELSE false END FROM UserPlan upr WHERE upr.travelPlan.id = :travelPlanId AND upr.user.id = :userId")
    boolean existsByUserIdAndTravelPlanId(@Param(value = "userId") UUID userId,
                                          @Param(value = "travelPlanId") Long travelPlanId);

    @Modifying
    void deleteByTravelPlanAndUser(TravelPlan travelPlan, User user);

    @Query("SELECT upr FROM UserPlan upr WHERE upr.travelPlan.id = :travelPlanId")
    List<UserPlan> findUserPlansByTravelPlan(Long travelPlanId);

    @Query("SELECT up.user.username FROM UserPlan up WHERE up.travelPlan.id = :travelPlanId")
    List<String> findUsersByTravelPlan(@Param(value = "travelPlanId") Long travelPlanId);

}