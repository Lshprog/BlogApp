package com.example.TravelPlanner.travelplanning.entities;
import com.example.TravelPlanner.auth.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_plans")
public class UserPlan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_plan_id", referencedColumnName = "id")
    @ToString.Exclude
    private TravelPlan travelPlan;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ToString.Exclude
    private User user;


}
