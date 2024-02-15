package com.example.TravelPlanner.travelplanning.repositories;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.entities.TravelPlan;
import com.example.TravelPlanner.travelplanning.entities.UserPlanRoles;
import com.example.TravelPlanner.travelplanning.common.enums.PlanRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPlanRolesRepository extends JpaRepository<UserPlanRoles, Long> {

    PlanRole findUserPlanRoleByUserAndTravelPlan(User user, TravelPlan travelPlan);

}
