package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPlanRolesRepository extends JpaRepository<UserPlanRoles, Long> {

    PlanRole findUserPlanRoleByUserAndTravelPlan(User user, TravelPlan travelPlan);

    Optional<UserPlanRoles> findUserPlanRolesByTravelPlanAndRole(TravelPlan travelPlan, PlanRole planRole);

    void deleteByTravelPlanAndUser(TravelPlan travelPlan, User user);

    @Query("SELECT upr FROM UserPlanRoles upr WHERE upr.travelPlan.id = :travelPlanId")
    List<UserPlanRoles> findUserPlanRolesByTravelPlan(Long travelPlanId);

}
