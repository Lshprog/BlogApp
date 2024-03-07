package com.example.TravelPlanner.travelplanning.entities;

import com.example.TravelPlanner.auth.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "travel_plans")
public class TravelPlan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Event> events = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "join_code")
    private String joinCode;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "title", nullable = false)
    private String title;

    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UserPlanRoles> userPlanRoles = new HashSet<>();

    @Override
    public String toString() {
        return "TravelPlan{" +
                "id=" + id +
                ", owner=" + owner.getUsername() +
                ", joinCode='" + joinCode + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                '}';
    }
}
