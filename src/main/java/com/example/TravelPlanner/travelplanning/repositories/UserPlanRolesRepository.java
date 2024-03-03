package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPlanRolesRepository extends JpaRepository<UserPlanRoles, Long> {

    Optional<PlanRole> findUserPlanRoleByUserAndTravelPlan(User user, TravelPlan travelPlan);

    Optional<UserPlanRoles> findUserPlanRolesByTravelPlanAndRole(TravelPlan travelPlan, PlanRole planRole);

    @Query("SELECT upr FROM UserPlanRoles upr WHERE upr.travelPlan.id = :travelPlanId AND upr.user.id = :userId")
    UserPlanRoles getUserPlanRoleByUserIdAndTravelPlanId(@Param(value = "travelPlanId") Long travelPlanId,
                                                         @Param(value = "userId") UUID userId);

    @Query("SELECT CASE WHEN COUNT(upr) > 0 THEN true ELSE false END FROM UserPlanRoles upr WHERE upr.travelPlan.id = :travelPlanId AND upr.user.id = :userId")
    boolean existsByUserIdAndTravelPlanId(@Param(value = "travelPlanId") Long travelPlanId,
                                          @Param(value = "userId") UUID userId);

    void deleteByTravelPlanAndUser(TravelPlan travelPlan, User user);

    @Query("SELECT upr FROM UserPlanRoles upr WHERE upr.travelPlan.id = :travelPlanId")
    List<UserPlanRoles> findUserPlanRolesByTravelPlan(Long travelPlanId);

}
