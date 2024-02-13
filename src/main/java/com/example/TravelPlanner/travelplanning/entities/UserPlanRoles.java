package com.example.TravelPlanner.travelplanning.entities;

import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.travelplanning.enums.PlanRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plans_users")
public class UserPlanRoles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_plan_id", referencedColumnName = "id")
    private TravelPlan travelPlan;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PlanRole role;

    // Other fields and methods
}

